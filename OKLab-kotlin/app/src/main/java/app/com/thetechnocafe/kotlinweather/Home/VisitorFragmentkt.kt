package com.playcorp.sqapp.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.thetechnocafe.kotlinweather.Home.BaseRecyclerExtendsAdapter
import app.com.thetechnocafe.kotlinweather.Home.CropCircleTransform
import app.com.thetechnocafe.kotlinweather.Home.ReloadRecyclerViewScrollListner
import app.com.thetechnocafe.kotlinweather.Models.VisitorInfo
import app.com.thetechnocafe.kotlinweather.Networking.NetworkService
import app.com.thetechnocafe.kotlinweather.Networking.ResVisitorList
import app.com.thetechnocafe.kotlinweather.R
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_visitor.*
import kotlinx.android.synthetic.main.item_visitor_list.view.*
import kotlinx.android.synthetic.main.layout_visitor.view.*
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by arent on 2017. 7. 11..
 */
class VisitorFragmentkt : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mListAdapter: VisitorAdapter
    lateinit var mScrollListener: ReloadRecyclerViewScrollListner

    private val mUserList = ArrayList<VisitorInfo>()

    private var mFirstSeq: Long = 0 // 리스트의 하단 마지막 listSeq loadMore용
    private var mIsExecuteFadeAnim = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("", " ++ onCreateView ++")
        return inflater?.inflate(R.layout.layout_visitor,container,false)!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirst()

        RL_VISITOR_MAIN.setBackgroundResource(R.color.cardview_light_background)

    }

    private fun initFirst(){
        mFirstSeq = 0
        mIsExecuteFadeAnim = false

        setSwipeToRefresh()
        setRecyclerView()

        getVisitorList()
    }

    override fun onRefresh() {
        mFirstSeq = 0
        mIsExecuteFadeAnim = false
        mScrollListener?.reset()
        swipeRefreshLayout?.isEnabled = false
        getVisitorList()
    }


    private fun setRecyclerView() {
        val manager = LinearLayoutManager(activity.applicationContext)
        manager.orientation = LinearLayoutManager.VERTICAL
        RCV_VISITOR_LISTVIEW.layoutManager = manager
        RCV_VISITOR_LISTVIEW.setHasFixedSize(true)
        mListAdapter = VisitorAdapter(mUserList)

        // LoadMore
        mScrollListener = object : ReloadRecyclerViewScrollListner(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (mListAdapter == null)
                    return
                if ( mFirstSeq > mListAdapter?.pageItemCountByLoadMore as Long ) {
                    // PREVIE모드면 로그인창으로 유도.
                } else {
                    getVisitorList()
                }
            }

            override fun onScrolledExt(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //                Logger.d("onScrolledExt", "dy = " + dy);
                mIsExecuteFadeAnim = true
            }

        }
        RCV_VISITOR_LISTVIEW.addOnScrollListener(mScrollListener)
        mListAdapter?.pageItemCountByLoadMore = 30
        RCV_VISITOR_LISTVIEW.itemAnimator = null
//        val pixel = resources.getDimensionPixelSize(R.dimen.user_list_item_bottom_margin_height)
//        RCV_VISITOR_LISTVIEW.addItemDecoration(SpacesItemDecoration(pixel))
//        SlideInBottomAnimationAdapter slideUpAdapter = new SlideInBottomAnimationAdapter(mListAdapter);
//        slideUpAdapter.setFirstOnly(true);
//        slideUpAdapter.setDuration(800);
//        slideUpAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        RCV_VISITOR_LISTVIEW.adapter = mListAdapter
    }

    private fun getVisitorList() {
        val paraMap = HashMap<String,Any>()
        paraMap.put("firstSeq",mFirstSeq)
        NetworkService.getSaycupidApi()
                .getVisitorList(paraMap)
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
        Log.d("Network","getVisitorList = start ")
    }

    private fun onNetFail(){

    }

    private fun onNetSuccess(data: ResVisitorList){
        val result = data
        val arrUserInfos = result.list
        if (arrUserInfos?.size != null && arrUserInfos.size > 0) {
            if (mListAdapter != null) {
                if (mFirstSeq == 0L) { // 초기화
                    //                            mListAdapter.clear();
                    mListAdapter?.data = arrUserInfos
                } else {
                    mListAdapter?.addItemAll(arrUserInfos)
                }
                mFirstSeq = arrUserInfos?.get(arrUserInfos.size - 1)?.seq as Long
            }
        } else {
            mListAdapter?.setVisibleFooterView(false)
        }
        // 데이터 없음
        if (mUserList != null && mUserList.size == 0) {
            RL_VISITOR_NONE_BOX.visibility = View.VISIBLE
            RL_VISITOR_EXIST_BOX.visibility = View.GONE
        } else {
            RL_VISITOR_NONE_BOX.visibility = View.GONE
            RL_VISITOR_EXIST_BOX.visibility = View.VISIBLE
//                resetNewMsgEachCounts(AppConst.FRAGMENT_VISITOR, 0)
        }
        swipeRefreshLayout?.isRefreshing = false  // Refresh Finished
        swipeRefreshLayout?.isEnabled = true
    }

    private fun setSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this)
        //        mSwipeRefreshView.setProgressViewOffset(false, 0 , 300);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    inner class VisitorAdapter(data: List<VisitorInfo>): BaseRecyclerExtendsAdapter<VisitorInfo>(data) {

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            //            Glide.clear(holder.itemView.findViewById(R.id.IV_USERLIST_PROFILE_IMAGE));
        }

        override fun onBindViewHolderImpl(viewHolder: RecyclerView.ViewHolder, adapter: BaseRecyclerExtendsAdapter<VisitorInfo>, i: Int) {
            // If you're using your custom handler (as you should of course)
            // you need to cast viewHolder to it.
            val visitorInfo = data.get(i)
            visitorInfo.seq = i.toLong()

            // View Set
            val strProfileURL =
                    Glide.with(activity.applicationContext)
                            .load("http://static.saycupid.com/images/member_pic/2016/04/18/11680359_1460961395421.jpg")
                            .bitmapTransform(CropCircleTransform(activity.applicationContext))
                            .crossFade(300)
                            .fallback(R.drawable.ic_drop)
                            .error(R.drawable.ic_close)
                            .into(viewHolder.itemView.IV_VISITOR_PROFILE)

            // regDate & Divider 처리
            setViewDateNDivider(viewHolder as MyCustomViewHolder, visitorInfo, i)

            viewHolder.itemView.TV_VISITOR_NAME.text = visitorInfo.user_login_id

            val location = visitorInfo.user_home_addr1
            val nAge = visitorInfo.user_age
            viewHolder.itemView.TV_VISITOR_LOCATIONAGE.text = location + " / " + nAge

            // Click Event
            viewHolder.itemView.RL_VISITOR_ITEM_BOX.setOnClickListener({

            })

            viewHolder.itemView.RL_VISITOR_ITEMVIEW.setBackgroundResource(R.color.abc_search_url_text_normal)

        }

        override fun onCreateViewHolderImpl(viewGroup: ViewGroup, adapter: BaseRecyclerExtendsAdapter<VisitorInfo>, i: Int): MyCustomViewHolder {
            // Here is where you inflate your row and pass it to the constructor of your ViewHolder
            val view = MyCustomViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_visitor_list, viewGroup, false))
            return view
        }

        private fun setViewDateNDivider(viewHolder: MyCustomViewHolder, userInfo: VisitorInfo, position: Int) {

            var beforeInfo: VisitorInfo? = null
            var nextInfo: VisitorInfo? = null


            viewHolder.itemView.TV_VISITOR_DATE.visibility = View.VISIBLE
            viewHolder.itemView.IV_VISITOR_DIVIDER.visibility = View.VISIBLE

        }

    }

    class MyCustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }
    }
}