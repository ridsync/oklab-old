//
//  ImageViewController.swift
//  
//
//  Created by 에이렌트 on 2017. 3. 22..
//
//

import UIKit

class ImageDetailController: UIViewController , UIScrollViewDelegate {
    
    var selectImage: UIImage? = nil
    @IBOutlet weak var scrollView: UIScrollView!
    
    let imageView = UIImageView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        self.scrollView.minimumZoomScale=0.5;
        
        self.scrollView.maximumZoomScale=6.0;
      
        
        self.scrollView.delegate=self;
        
       
        imageView.image = selectImage
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = false;
        self.scrollView.addSubview(imageView)
        
        self.scrollView.contentSize=CGSize(width:imageView.frame.width, height:imageView.frame.height);
        
        imageView.translatesAutoresizingMaskIntoConstraints = false
        // Constraint
        let leading = NSLayoutConstraint(item: imageView,
                                         attribute: .width,
                                         relatedBy: .equal,
                                         toItem: self.scrollView,
                                         attribute: .width,
                                         multiplier: 1.0,
                                         constant: 0.0)
        let trailing = NSLayoutConstraint(item: imageView,
                                          attribute: .height,
                                          relatedBy: .equal,
                                          toItem: self.scrollView,
                                          attribute: .height,
                                          multiplier: 1.0,
                                          constant: 0.0)

        let top = NSLayoutConstraint(
            item: imageView,
            attribute: NSLayoutAttribute.centerX,
            relatedBy: NSLayoutRelation.equal,
            toItem: self.scrollView,
            attribute: NSLayoutAttribute.centerX,
            multiplier: 1.0,
            constant: 0.0)
        let bottom = NSLayoutConstraint(
            item: imageView,
            attribute: NSLayoutAttribute.centerY,
            relatedBy: NSLayoutRelation.equal,
            toItem: self.scrollView,
            attribute: NSLayoutAttribute.centerY,
            multiplier: 1.0,
            constant: 0.0)
        self.scrollView.addConstraints([leading,trailing,top,bottom])

        
    }
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
  
    return self.imageView;
    
    }
    
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
    
}

