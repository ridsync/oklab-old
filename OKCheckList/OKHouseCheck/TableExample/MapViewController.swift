//
//  MapViewController.swift
//  TableExample
//
//  Created by 에이렌트 on 2017. 3. 21..
//  Copyright © 2017년 Example. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit

class MapViewController: UIViewController  , CLLocationManagerDelegate{

    @IBOutlet weak var mapView: MKMapView!
    
    let locationManager = CLLocationManager()
    var locationCenter:CLLocationCoordinate2D? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.locationManager.delegate = self
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest
        self.locationManager.requestWhenInUseAuthorization()
        self.mapView.showsUserLocation = true
        // Do any additional setup after loading the view.
        
        if let center = locationCenter {
            let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.02, longitudeDelta: 0.02))
            
            self.mapView.setRegion(region, animated: true)
            self.locationManager.stopUpdatingLocation()
            
            // Add Pin Here
            let annotation = MKPointAnnotation()
            annotation.coordinate = CLLocationCoordinate2D(latitude:center.latitude, longitude: center.longitude)
            annotation.title = "Here"
            self.mapView.addAnnotation(annotation)
            
        } else {
            
            self.locationManager.startUpdatingLocation()
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location = locations.last
       let center = CLLocationCoordinate2D(latitude: location!.coordinate.latitude, longitude: location!.coordinate.longitude)
        
        locationCenter = center
        
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.02, longitudeDelta: 0.02))
        
        self.mapView.setRegion(region, animated: true)
        self.locationManager.stopUpdatingLocation()
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Errors " + error.localizedDescription)
    }
    @IBAction func currentLocation(_ sender: Any) {
        
        self.locationManager.startUpdatingLocation()
        
    }
    @IBAction func saveLocation(_ sender: UIBarButtonItem) {
        
        
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
