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
import app.com.thetechnocafe.kotlinweather.OKKotlin
import app.com.thetechnocafe.kotlinweather.models.ResTimeLineList
import app.com.thetechnocafe.kotlinweather.models.TimeLineInfo
import app.com.thetechnocafe.kotlinweather.networking.NetworkService
import app.com.thetechnocafe.kotlinweather.R
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_timeline.*
import kotlinx.android.synthetic.main.item_timeline_list.view.*
import kotlinx.android.synthetic.main.layout_timeline.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by arent on 2017. 7. 20..
 */
class TimeLineActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener {

//    var binding: LayoutTimelineBinding? = null
    private var mIsExecuteFadeAnim: Boolean = false

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

        SWC_setting_notify_info.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("TimeLineActivity","onClickGoTop isChecked $isChecked")
        }
        BTN_newfeed_top.setOnClickListener {
            RCV_timeline.smoothScrollToPosition(0)
        }
        setSwipeToRefresh()
        setRecyclerView()

        getTimeLineList()

        initDb()

    }

    private fun initDb() {

        async(UI) {
            bg {
                val listTimeLine =  OKKotlin.db.timeLineDao().getAll()

                Log.d("async","list = ${listTimeLine.size} / ${listTimeLine}")
            }
        }

//        doAsync {
//            val listTimeLine =  OKKotlin.db.timeLineDao().getAll()
//
//            Log.d("doAsync","list = ${listTimeLine.size} / ${listTimeLine}")
//            uiThread {
//
//            }
//        }

    }

    override fun onRefresh() {
        mListAdapter.mFirstSeq = 0
        mIsExecuteFadeAnim = false
        mScrollListener.reset()
        SRL_swipeRefresh?.isEnabled = false
        getTimeLineList()
    }

    private fun setRecyclerView() {
        val manager = LinearLayoutManager(applicationContext)
                .apply { orientation = LinearLayoutManager.VERTICAL }
        RCV_timeline.layoutManager = manager
        RCV_timeline.setHasFixedSize(true)
        mListAdapter = TimeLineAdapter()
        mListAdapter.pageItemCountByLoadMore = 30
        // LoadMore
        mScrollListener = object : ReloadRecyclerViewScrollListner(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
               val pageCountUnit = mListAdapter.pageItemCountByLoadMore
                if ( mListAdapter.mFirstSeq > 0 ) {
                    getTimeLineList()
                }
            }

            override fun onScrolledExt(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //                Logger.d("onScrolledExt", "dy = " + dy);
                mIsExecuteFadeAnim = true
            }

        }
        RCV_timeline.addOnScrollListener(mScrollListener)
        RCV_timeline.itemAnimator = null
//        val pixel = resources.getDimensionPixelSize(R.dimen.user_list_item_bottom_margin_height)
//        RCV_timeline.addItemDecoration(SpacesItemDecoration(pixel))
//        SlideInBottomAnimationAdapter slideUpAdapter = new SlideInBottomAnimationAdapter(mListAdapter);
//        slideUpAdapter.setFirstOnly(true);
//        slideUpAdapter.setDuration(800);
//        slideUpAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        RCV_timeline.adapter = mListAdapter
    }

    private fun getTimeLineList(){
        val paraMap = HashMap<String,Any>()
        paraMap.put("firstSeq",mListAdapter.mFirstSeq)
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
        SRL_swipeRefresh?.isRefreshing = false  // Refresh Finished
        SRL_swipeRefresh?.isEnabled = true
    }

    private fun onNetSuccess(data: ResTimeLineList){

        mListAdapter.setListData(data.list)
        data.list?.let {

            async(UI) {
                bg {
                    for ( time in it ) {
                        OKKotlin.db.timeLineDao().insertAll( time )
                    }

                    val listTimeLine =  OKKotlin.db.timeLineDao().getAll()
                    Log.d("async","listTimeLine list = ${listTimeLine.size} / ${listTimeLine.toString()}")
                }
            }

        }

        SRL_swipeRefresh?.isEnabled = true
        SRL_swipeRefresh?.isRefreshing = false  // Refresh Finished

    }

    private fun setSwipeToRefresh() {
        SRL_swipeRefresh.setOnRefreshListener(this)
        //        mSwipeRefreshView.setProgressViewOffset(false, 0 , 300);
        SRL_swipeRefresh.setColorSchemeResources(R.color.colorAccent)
    }

    inner class TimeLineAdapter : BaseRecyclerExtendsAdapter<TimeLineInfo> {

        var mFirstSeq: Long = 0
        var mLastSeq: Long = 0

        constructor() : super(ArrayList<TimeLineInfo>())

//        constructor(data: List<TimeLineInfo>) : super(data)

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
        }

        override fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: BaseRecyclerExtendsAdapter<TimeLineInfo>, i: Int) {
            // If you're using your custom handler (as you should of course)
            // you need to cast viewHolder to it.
            val visitorInfo = data[i]
            visitorInfo.seq = i.toLong()

            // View Set
            Glide.with(applicationContext)
                            .load("http://static.saycupid.com/images/member_pic/" + visitorInfo.user_photo)
                            .bitmapTransform(CropCircleTransform(applicationContext))
                            .crossFade(300)
                            .fallback(R.drawable.ic_drop)
                            .error(R.drawable.ic_close)
                            .into(viewHolder.itemView.IV_profile_image)

            // regDate & Divider 처리
            setViewDateNDivider(viewHolder as MyCustomViewHolder, visitorInfo, i)

            viewHolder.itemView.TV_userinfo_username.text = visitorInfo.user_login_id

            viewHolder.itemView.TV_userinfo_purpose.text = visitorInfo.desc

            // Click Event
            viewHolder.itemView.LL_item_box.setOnClickListener({

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

            viewHolder.itemView.TV_time_elapsed.visibility = View.VISIBLE

        }

        fun setListData(items:List<TimeLineInfo>? ){
            val itemSize = items?.size ?: 0

            if (itemSize > 0) {
                if (mFirstSeq == 0L) { // 초기화
                    data = items
                } else {
                    addItemAll(items)
                }
                mLastSeq = data[0].seq
                mFirstSeq = data[data.size - 1].seq
            }
            if (itemSize >= pageItemCountByLoadMore) {
                setVisibleFooterView(true)
            } else {
                setVisibleFooterView(false)
            }
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