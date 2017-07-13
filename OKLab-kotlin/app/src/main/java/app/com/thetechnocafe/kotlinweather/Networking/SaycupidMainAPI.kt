package app.com.thetechnocafe.kotlinweather.Networking

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

/**
 * Created by gurleensethi on 16/06/17.
 */
interface SaycupidMainAPI {
    @Headers("userId: 11646376", "appVer: 0.1.445","uuid: 0FD3574E-068E-41C9-9329-EBBDA9901B41")
    @GET("/profile/getListVisit")
    fun getVisitorList(@QueryMap params: HashMap<String, Any>): Observable<ResVisitorList>
}