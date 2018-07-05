//
//  DetailViewController.swift
//  TableExample
//
//  Created by 에이렌트 on 2017. 3. 20..
//  Copyright © 2017년 Example. All rights reserved.
//

import UIKit
import CoreData
import DatePickerDialog
import XCGLogger
import Foundation
import CoreLocation
import Alamofire
import AlamofireImage

class DetailViewController: UIViewController , UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    
    @IBOutlet weak var mainScrollView: UIScrollView!
 
    var isNewFileimage: Bool = false
    var selectHouse: House?
    var moContext : NSManagedObjectContext!
    var imageArray = [UIImage]()
    var newFileUrl:String? = nil
    var oldFileUrl:String? = nil
    var latitude:Double? = nil
    var longitude:Double? = nil
    
    @IBOutlet weak var picArea: UIView!
    
    @IBOutlet weak var scrolllPicView: UIScrollView!

    
    @IBOutlet weak var houseName: UITextField!
    
    @IBOutlet weak var price: UITextField!
    @IBOutlet weak var area: UITextField!
 
    @IBOutlet weak var date: UITextField!
  
    @IBOutlet weak var location: UITextField!
    @IBOutlet weak var address: UITextField!
    @IBOutlet weak var segSchool: UISegmentedControl!

    @IBOutlet weak var segTraffic: UISegmentedControl!
  
    @IBOutlet weak var segLocation: UISegmentedControl!

    @IBOutlet weak var segEnvironments: UISegmentedControl!

    @IBOutlet weak var segRightSunView: UISegmentedControl!
    @IBOutlet weak var tfEnvironment: UITextField!

    @IBOutlet weak var segInterior: UISegmentedControl!

    @IBOutlet weak var segFacilityControl: UISegmentedControl!
 
    @IBOutlet weak var segRoomCount: UISegmentedControl!

    @IBOutlet weak var segRestRoomCount: UISegmentedControl!

    @IBOutlet weak var manageCost: UITextField!
    @IBOutlet weak var parking: UITextField!
    @IBOutlet weak var building: UITextField!
    @IBOutlet weak var etc: UITextView!
    
    
    @IBAction func addHouseInfo(_ sender: UIBarButtonItem) {
        
        if let selhouse = selectHouse {
            
            saveHouseInfo(house: selhouse)
        } else {
            
            if #available(iOS 10.0, *) {
                //                moContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
                let house = House(context: moContext)
                saveHouseInfo(house: house)
            } else if #available(iOS 9.0, *){
                //                moContext = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
                let house = NSEntityDescription.insertNewObject(forEntityName: "House", into: self.moContext!) as! House
                saveHouseInfo(house: house)
            }
            
        }
        
    }
    
    @IBAction func datePicker(_ sender: Any) {
        showDatePicker()
    }
    
    @IBAction func addPicture(_ sender: Any) {
        
        let pickerAlert = UIAlertController(title: nil, message: nil, preferredStyle: UIAlertControllerStyle.actionSheet)
        pickerAlert.addAction(UIAlertAction(title: "Gallery", style: .default, handler: {
            action in
            let imagePickerController = UIImagePickerController()
            
            // Only allow photos to be picked, not taken.
            imagePickerController.sourceType = .photoLibrary
            
            // Make sure ViewController is notified when the user picks an image.
            imagePickerController.delegate = self
            
            self.present(imagePickerController, animated: true, completion: nil)
        }))
        pickerAlert.addAction(UIAlertAction(title: "Camera", style: .default, handler: {
            action in
            let imagePickerController = UIImagePickerController()
            
            // Only allow photos to be picked, not taken.
            imagePickerController.sourceType = .camera
            imagePickerController.allowsEditing = true
            
            // Make sure ViewController is notified when the user picks an image.
            imagePickerController.delegate = self
            
            self.present(imagePickerController, animated: true, completion: nil)
        }))
        pickerAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        self.present(pickerAlert, animated: true, completion:{})
        
    }
    
    @IBAction func dateCheck(_ sender: Any) {
        
    }

    @IBAction func deleteHouse(_ sender: UIButton) {
        
        if self.selectHouse != nil {
            
            let editRadiusAlert = UIAlertController(title: "주의하세요", message: "진짜로 삭제 할거에요?", preferredStyle: UIAlertControllerStyle.alert)
            editRadiusAlert.addAction(UIAlertAction(title: "삭제", style: .destructive, handler: { alert -> Void in
                
                
                self.moContext.delete(self.selectHouse!)
                let _ = self.navigationController?.popViewController(animated: true)

                
            }))
            editRadiusAlert.addAction(UIAlertAction(title: "취소", style: .cancel, handler: nil))
            
            self.present(editRadiusAlert, animated: true, completion:{})

            
        }
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
                
        registerForKeyboardDidShowNotification(scrollView: self.mainScrollView)
        registerForKeyboardWillHideNotification(scrollView: self.mainScrollView)
        
        // CoreData moContext 초기화
        if #available(iOS 10.0, *) {
            self.moContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
            
        } else if #available(iOS 9.0, *){
            self.moContext = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
        }
        
        loadHouseInfo()
        
        self.navigationItem.backBarButtonItem?.title = " "
        self.navigationController?.navigationBar.backItem?.title = " "
        
    }
    
//    override func viewDidAppear(_ animated: Bool) {
//        
//        setNavigationBarEffectViewSend()
//        
//    }
 
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
        /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    func loadHouseInfo(){
        imageArray.removeAll()
        
        // Do any additional setup after loading the view.
        if let houseInfo = selectHouse {
            self.navigationItem.title = houseInfo.houseName
            
            self.houseName.text = houseInfo.houseName
            self.price.text = houseInfo.price
            self.area.text = houseInfo.area
            self.date.text = houseInfo.date
            self.location.text = houseInfo.location
            self.address.text = houseInfo.address
            self.segSchool.selectedSegmentIndex = Int(houseInfo.segSchool)
            self.segTraffic.selectedSegmentIndex = Int(houseInfo.segTraffic)
            self.segLocation.selectedSegmentIndex = Int(houseInfo.segLocation)
            self.segEnvironments.selectedSegmentIndex = Int(houseInfo.segEnvironment)
            self.tfEnvironment.text = houseInfo.environment
            self.segRightSunView.selectedSegmentIndex = Int(houseInfo.segRightSunView)
            self.segInterior.selectedSegmentIndex = Int(houseInfo.segInterior)
            self.segFacilityControl.selectedSegmentIndex = Int(houseInfo.segFacilityControl)
            self.segRoomCount.selectedSegmentIndex = Int(houseInfo.segRoomCount)
            self.segRestRoomCount.selectedSegmentIndex = Int(houseInfo.segRestroomCount)
            self.manageCost.text = houseInfo.manageCost
            self.parking.text = houseInfo.parking
            self.building.text = houseInfo.building
            self.etc.text = houseInfo.etc
            
            self.latitude = houseInfo.latitude
            self.longitude = houseInfo.longitude
            // ImageFile
            
            if let fileUrl = houseInfo.fileUrl {
                
                let fileManager = FileManager.default
                let imagePAth = self.getDocumentsDirectory().appendingPathComponent(fileUrl)
                if fileManager.fileExists(atPath: imagePAth.path){
                    if let image = UIImage(contentsOfFile: imagePAth.path) {
                        addPictureInScrollView(image:image)
                    }
                self.oldFileUrl = fileUrl
                self.isNewFileimage = false
                } else{
                    print("No Image File")
                }
                
            }
            
        } else {
            // New House Item
            self.navigationItem.title = "New House"
        }

    }
    
    func saveHouseInfo(house: House) {
        
        // TODO: Picture Path
        house.houseName = self.houseName.text!
        house.price = self.price.text!
        house.area = self.area.text!
        // TODO: date
        house.date = self.date.text!
        house.location = self.location.text!
        // TODO: GPS Point
        house.address = self.address.text!
        house.segSchool = Int16(self.segSchool.selectedSegmentIndex)
        house.segTraffic = Int16(self.segTraffic.selectedSegmentIndex)
        house.segLocation = Int16(self.segLocation.selectedSegmentIndex)
        house.segEnvironment = Int16(self.segEnvironments.selectedSegmentIndex)
        house.environment = self.tfEnvironment.text!
        house.segRightSunView = Int16(self.segRightSunView.selectedSegmentIndex)
        house.segInterior = Int16(self.segInterior.selectedSegmentIndex)
        house.segFacilityControl = Int16(self.segFacilityControl.selectedSegmentIndex)
        house.segRoomCount = Int16(self.segRoomCount.selectedSegmentIndex)
        house.segRestroomCount = Int16(self.segRestRoomCount.selectedSegmentIndex)
        house.manageCost = self.manageCost.text!
        house.parking = self.parking.text!
        house.building = self.building.text!
        house.etc = self.etc.text!
        
        if self.latitude != nil {
            house.latitude = self.latitude!
        }
        if self.longitude != nil {
             house.longitude = self.longitude!
        }
       
        if selectHouse == nil {
            house.regDate = NSDate()
        }
        
        if(self.isNewFileimage){
            house.fileUrl = newFileUrl
        } else if self.oldFileUrl != nil {
            house.fileUrl = oldFileUrl
        } else {
            house.fileUrl = nil
        }

        house.totalScore = house.segSchool + house.segLocation + house.segEnvironment
            + house.segRightSunView + house.segInterior + house.segFacilityControl
            + house.segRoomCount + house.segRestroomCount + house.segTraffic
            + 9
        // Save the data to coredata
        
        (UIApplication.shared.delegate as! AppDelegate).saveContext()
        let _ = navigationController?.popViewController(animated: true)
    }
    
    func showDatePicker(){
        
        
        DatePickerDialog().show(title: "날짜 선택", doneButtonTitle: "확인", cancelButtonTitle: "취소", datePickerMode: .date) {
            (date) -> Void in
            
            if date != nil {
                let df : DateFormatter = DateFormatter()
                df.dateFormat = "yyyy년 MM월 dd일"
                let strDate = df.string(from: date!)
                
                self.date.text = strDate
            }
            
        }
     
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
       
        logger.debug("[ImageSave] imagePickerController \(self.scrolllPicView.subviews.count) ")
        
        if let image = info[UIImagePickerControllerOriginalImage] as? UIImage {
            //            imageView.frame = CGRect(x: 0, y: 0, width: 100, height: 100);
            //            imageView.frame.size.width = 100
            //            imageView.frame.size.height = 200
            addPictureInScrollView(image: image)
            
            let fileName = fileSave(image:image,pathName: "testfile", index: scrolllPicView.subviews.count )
            logger.debug("[ImageSave] fileName = \(fileName)")
            
            let image = getImageFromURL(fileName: fileName)
            
            newFileUrl = fileName
            self.isNewFileimage = true
            logger.debug("[ImageSave] getImageFromURL \(image)")
        } else{
            logger.debug("[ImageSave] Something went wrong")
        }

        //
        self.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
        logger.debug("[ImageSave] imagePickerControllerDidCancel ")
    }
    
    func fileSave(image:UIImage,pathName:String,index:Int) -> String{

        do {
            
            let fileName = "\(pathName)-\(index).png"
            let filePath = self.getDocumentsDirectory().appendingPathComponent(fileName)
            
            if let pngImageData = UIImagePNGRepresentation(image) {
                try pngImageData.write(to: filePath, options: .atomic)
            }
            return fileName
        } catch {
            print(error)
            return ""
        }
        
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let documentsDirectory = paths[0]
        return documentsDirectory
    }
    
    func getImageFromURL(fileName:String) -> UIImage?{
        
        let nsDocumentDirectory = FileManager.SearchPathDirectory.documentDirectory
        let nsUserDomainMask    = FileManager.SearchPathDomainMask.userDomainMask
        let paths               = NSSearchPathForDirectoriesInDomains(nsDocumentDirectory, nsUserDomainMask, true)
        if let dirPath          = paths.first
        {
            let imageURL = URL(fileURLWithPath: dirPath).appendingPathComponent(fileName)
            let image    = UIImage(contentsOfFile: imageURL.path)
            // Do whatever you want with the image
            return image
        }
        return nil
    }
    
    // MARK TODO: 초기화시 이미지추가하기전에 스크롤뷰에 이미 2개가 들어있는 이유는?
    func addPictureInScrollView(image:UIImage){
        // MARK: 기존 이미지 지우고 한개만 등록하도록 임시...
        for subview in self.scrolllPicView.subviews {
            subview.removeFromSuperview()
        }
        imageArray.removeAll()
        
        imageArray.append(image)
        
        let imageView = UIImageView()
        
//        let url = URL(image)!
//        let placeholderImage = UIImage(named: "placeholder")!
//        
//        let filter = AspectScaledToFillSizeWithRoundedCornersFilter(
//            size: (imageView.frame.size),
//            radius: 20.0
//        )
//        
//        imageView.af_setImage(
//            withURL: url,
//            placeholderImage: placeholderImage,
//            filter: filter
//        )
//
        let picWidth = self.picArea.frame.width / 6
        let xPosition = picWidth * CGFloat(self.scrolllPicView.subviews.count)
        imageView.frame = CGRect(x: xPosition, y: 0, width: picWidth, height: self.picArea.frame.height)
        
        imageView.image = image
        imageView.layer.cornerRadius = picWidth / 2;
        imageView.layer.masksToBounds = true;
        imageView.contentMode = .scaleToFill
        //        imageView.contentMode = .center
       
        // add
        self.scrolllPicView.contentSize.width = picWidth * CGFloat(scrolllPicView.subviews.count + 1)
        self.scrolllPicView.addSubview(imageView)
        // Gesture
       let tapGesture = UITapGestureRecognizer(target: self, action: #selector(tapImageView) )
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(tapGesture)
        
        let longTabGesture = UILongPressGestureRecognizer(target: self, action:  #selector(longTapImageView) )
        imageView.addGestureRecognizer(longTabGesture)
    }
    
    func tapImageView(sender: UITapGestureRecognizer){
        
        let imageView = sender.view as! UIImageView
        
        if let ivControl = storyboard?.instantiateViewController(withIdentifier: "ivController") as? ImageDetailController {
//            ivControl.selectImage = imageArray[0] // 첫번째
            ivControl.selectImage =  imageView.image!
           
            self.navigationController?.pushViewController(ivControl, animated: true)
//            self.present(ivControl, animated: true, completion: nil)
        }
        
    }
    
    func longTapImageView(sender: UITapGestureRecognizer){
        
        if ( imageArray.count > 0){
            imageArray.removeAll()
            for subview in self.scrolllPicView.subviews {
                subview.removeFromSuperview()
            }
            self.isNewFileimage = false
            self.oldFileUrl = nil
            self.newFileUrl = nil
        }
        
    }

    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "segueMapview" {
            if let devc = segue.destination as? MapViewController {
                if let latitude = self.latitude , let longitude = self.longitude {
                        devc.locationCenter = CLLocationCoordinate2D(latitude:latitude, longitude: longitude)
                
                }
            }
        }
    }

    
    func unwindToDetailView(_ unwindSegue: UIStoryboardSegue) {
        print("Called goToSideMenu: unwind action")
        
        if let svc = unwindSegue.source as? MapViewController {
            print("Coming from MapViewController")
            
            self.latitude = (svc.locationCenter?.latitude)!
            self.longitude = (svc.locationCenter?.longitude)!
        }
        
    }
    
    

}


