package app.com.thetechnocafe.kotlinweather.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.thetechnocafe.kotlinweather.models.ResTimeLineList
import app.com.thetechnocafe.kotlinweather.models.TimeLineInfo
import app.com.thetechnocafe.kotlinweather.networking.NetworkService
import app.com.thetechnocafe.kotlinweather.R
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_timeline.*
import kotlinx.android.synthetic.main.item_timeline_list.view.*
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by arent on 2017. 7. 20..
 */
class TimeLineActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener {

//    var binding: LayoutTimelineBinding? = null
    private var mIsExecuteFadeAnim: Boolean = false
    private var mFirstSeq: Long = 0
    private var mUserList = ArrayList<TimeLineInfo>()

    lateinit var mListAdapter: TimeLineAdapter
    lateinit var mScrollListener: ReloadRecyclerViewScrollListner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_timeline)

        initFirst()
    }

    fun onClickGoTop(view: View){
        Log.d("TimeLineActivity","onClickGoTop")
    }

    fun initFirst (){

        SWC_SETTING_NOTIFY_NEW_MESSAGE.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("TimeLineActivity","onClickGoTop isChecked $isChecked")
        }
        setSwipeToRefresh()
        setRecyclerView()

        getTimeLineList()
    }

    override fun onRefresh() {
        mFirstSeq = 0
        mIsExecuteFadeAnim = false
        mScrollListener.reset()
        swipeRefreshLayout?.isEnabled = false
        getTimeLineList()
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(applicationContext)
                .apply { orientation = LinearLayoutManager.VERTICAL }
        RCV_TIMELINE_LISTVIEW.layoutManager = manager
        RCV_TIMELINE_LISTVIEW.setHasFixedSize(true)
        mListAdapter = TimeLineAdapter(mUserList)

        // LoadMore
        mScrollListener = object : ReloadRecyclerViewScrollListner(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
               val pageCountUnit = mListAdapter.pageItemCountByLoadMore
                if ( mFirstSeq > pageCountUnit ) {
                    // PREVIE모드면 로그인창으로 유도.
                } else {
                    getTimeLineList()
                }
            }

            override fun onScrolledExt(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //                Logger.d("onScrolledExt", "dy = " + dy);
                mIsExecuteFadeAnim = true
            }

        }
        RCV_TIMELINE_LISTVIEW.addOnScrollListener(mScrollListener)
        mListAdapter.pageItemCountByLoadMore = 30
        RCV_TIMELINE_LISTVIEW.itemAnimator = null
//        val pixel = resources.getDimensionPixelSize(R.dimen.user_list_item_bottom_margin_height)
//        RCV_TIMELINE_LISTVIEW.addItemDecoration(SpacesItemDecoration(pixel))
//        SlideInBottomAnimationAdapter slideUpAdapter = new SlideInBottomAnimationAdapter(mListAdapter);
//        slideUpAdapter.setFirstOnly(true);
//        slideUpAdapter.setDuration(800);
//        slideUpAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        RCV_TIMELINE_LISTVIEW.adapter = mListAdapter
    }

    private fun getTimeLineList(){
        val paraMap = HashMap<String,Any>()
        paraMap.put("firstSeq",mFirstSeq)
        paraMap.put("listType",1)
        paraMap.put("lastSeq",0)
        NetworkService.getSaycupidApi()
                .getTimeLineList(paraMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data ->
                    Log.d("Network","data = " + data)
                    this.onNetSuccess(data)
                }, {
                    error ->
                    //                    mView?.onError()
                    this.onNetFail()
                    Log.d("Network","error = " + error)
                })
        Log.d("Network","getTimeLineList = start ")
    }

    private fun onNetFail(){
        swipeRefreshLayout?.isRefreshing = false  // Refresh Finished
        swipeRefreshLayout?.isEnabled = true
    }

    private fun onNetSuccess(data: ResTimeLineList){
        val result = data
        val arrUserInfos = result.list
        if (arrUserInfos?.size != null && arrUserInfos.size > 0) {
                if (mFirstSeq == 0L) { // 초기화
                    //                            mListAdapter.clear();
                    mListAdapter.data = arrUserInfos
                } else {
                    mListAdapter.addItemAll(arrUserInfos)
                }
                mFirstSeq = arrUserInfos[arrUserInfos.size - 1].seq
        } else {
            mListAdapter.setVisibleFooterView(false)
        }

        swipeRefreshLayout?.isEnabled = true
        swipeRefreshLayout?.isRefreshing = false  // Refresh Finished

    }

    private fun setSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this)
        //        mSwipeRefreshView.setProgressViewOffset(false, 0 , 300);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    inner class TimeLineAdapter(data: List<TimeLineInfo>): BaseRecyclerExtendsAdapter<TimeLineInfo>(data) {

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            //            Glide.clear(holder.itemView.findViewById(R.id.IV_USERLIST_PROFILE_IMAGE));
        }

        override fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: BaseRecyclerExtendsAdapter<TimeLineInfo>, i: Int) {
            // If you're using your custom handler (as you should of course)
            // you need to cast viewHolder to it.
            val visitorInfo = data.get(i)
            visitorInfo.seq = i.toLong()

            // View Set
            val strProfileURL =
                    Glide.with(applicationContext)
                            .load("http://static.saycupid.com/images/member_pic/" + visitorInfo.user_photo)
                            .bitmapTransform(CropCircleTransform(applicationContext))
                            .crossFade(300)
                            .fallback(R.drawable.ic_drop)
                            .error(R.drawable.ic_close)
                            .into(viewHolder.itemView.IV_TIMELINE_PROFILE_IMAGE)

            // regDate & Divider 처리
            setViewDateNDivider(viewHolder as MyCustomViewHolder, visitorInfo, i)

            viewHolder.itemView.TV_TIMELINE_USERINFO_UNSERNAME.text = visitorInfo.user_login_id

            viewHolder.itemView.IV_TIMELINE_USERINFO_PURPOSE.text = visitorInfo.desc

            // Click Event
            viewHolder.itemView.LL_TIMELINE_ITEM_BOX.setOnClickListener({

            })

        }

        override fun onCreateViewHolderImpl(viewGroup: ViewGroup, adapter: BaseRecyclerExtendsAdapter<TimeLineInfo>, i: Int): MyCustomViewHolder {
            // Here is where you inflate your row and pass it to the constructor of your ViewHolder
            val view = MyCustomViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_timeline_list, viewGroup, false))
            return view
        }

        private fun setViewDateNDivider(viewHolder: MyCustomViewHolder, userInfo: TimeLineInfo, position: Int) {

            var beforeInfo: TimeLineInfo? = null
            var nextInfo: TimeLineInfo? = null

            viewHolder.itemView.TV_TIMELINE_TIME_ELAPSED.visibility = View.VISIBLE

        }

    }

    class MyCustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }
    }

    companion object {
        private val INTENT_USER_ID = "user_id"

        fun newIntent(context: Context, user_id: String): Intent {
            val intent = Intent(context, TimeLineActivity::class.java)
            intent.putExtra(INTENT_USER_ID, user_id)
            return intent
        }
    }

}