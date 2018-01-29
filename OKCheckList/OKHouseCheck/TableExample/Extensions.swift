//
//  Extensions.swift
//  youtube
//
//  Created by Brian Voong on 6/3/16.
//  Copyright © 2016 letsbuildthatapp. All rights reserved.
//

import UIKit


extension UIViewController {
    func registerForKeyboardDidShowNotification(scrollView: UIScrollView, usingBlock block: ((NSNotification) -> Void)? = nil) {
        NotificationCenter.default.addObserver(forName: NSNotification.Name.UIKeyboardDidShow, object: nil, queue: nil, using: { (notification) -> Void in
            let userInfo = notification.userInfo!
            let keyboardSize = (userInfo[UIKeyboardFrameBeginUserInfoKey] as AnyObject).cgRectValue.size
            let contentInsets = UIEdgeInsetsMake(scrollView.contentInset.top, scrollView.contentInset.left, keyboardSize.height, scrollView.contentInset.right)
            
            scrollView.isScrollEnabled = true
            scrollView.setContentInsetAndScrollIndicatorInsets(edgeInsets: contentInsets)
            block?(notification as NSNotification)
        })
    }
    
    func registerForKeyboardWillHideNotification(scrollView: UIScrollView, usingBlock block: ((NSNotification) -> Void)? = nil) {
        NotificationCenter.default.addObserver(forName: NSNotification.Name.UIKeyboardWillHide, object: nil, queue: nil, using: { (notification) -> Void in
            let contentInsets = UIEdgeInsetsMake(scrollView.contentInset.top, scrollView.contentInset.left, 0, scrollView.contentInset.right)
            scrollView.setContentInsetAndScrollIndicatorInsets(edgeInsets: contentInsets)
            scrollView.isScrollEnabled = false
            block?(notification as NSNotification)
        })
    }
    
    
    // ExtendBar를 위한 구분선 제거 및 Blur적용
     func setNavigationBarEffectViewSend(){
        let subvuiws = self.navigationController?.navigationBar.subviews
        
        for view in subvuiws! {
            if let effectView = view as? UIVisualEffectView {
                self.navigationController?.navigationBar.sendSubview(toBack: effectView)
            }
        }
    }
    
    
    func setNavigationBarExtendStyle(){
        
        UIApplication.shared.statusBarStyle = .default
        
        self.navigationController?.navigationBar.isTranslucent = true
        
        self.navigationController?.navigationBar.backgroundColor = UIColor.clear
        self.navigationController?.navigationBar.shadowImage = UIImage()
        //        //        // "Pixel" is a solid white 1x1 image.
        self.navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        //        //
        //        self.navigationController?.navigationBar.barTintColor = UIColor.clear // Background
        self.navigationController?.navigationBar.tintColor = UIColor.black // Item Color
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.black] // Title Color
        
        navigationBarBlurEffect()
        
    }
    
    func setNavigationBarDefatulStyle(){
        UIApplication.shared.statusBarStyle = .default
        
        //        self.navigationController?.navigationBar.isTranslucent = false
        //
        //        self.navigationController?.navigationBar.shadowImage = #imageLiteral(resourceName: "TransparentPixel")
        //////        // "Pixel" is a solid white 1x1 image.
        //        self.navigationController?.navigationBar.setBackgroundImage(#imageLiteral(resourceName: "Pixel")
        //, for: .default)
        //
        //        self.navigationController?.navigationBar.barTintColor = UIColor.white // Background
        self.navigationController?.navigationBar.tintColor = UIColor.black // Item Color
        self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.black] // Title Color
        
        
        self.navigationController?.navigationBar.viewWithTag(12)?.removeFromSuperview()
    }
    
    
    // NavigationBar Blur 적용 잘됩니다 + StatusBar !!!
    // 그렇지만 아래 extendsNav영역에도 따로 Blur 줄경우 각가 따로 적용되므로 Blur효과가 어색하다.
    // 결국 확장을 쓸경우엔 Blur를 넣지말거나, NavigationBar에 직접 Segment를 넣는방법을 해야함... 이것도 Navigation관리에 그닥 편한 방법은 아니다.
    func navigationBarBlurEffect() {
        // Add blur view
        self.navigationController?.navigationBar.isTranslucent = true
        var bounds = self.navigationController?.navigationBar.bounds as CGRect!
        let visualEffectView = UIVisualEffectView(effect: UIBlurEffect(style: .light))
        
        //        bounds?.offsetBy(dx: 0.0, dy: -20.0)
        //        bounds?.size.height = (bounds?.height)! + 20.0
        
        visualEffectView.frame = CGRect(x:0,y:-20.0, width:(bounds?.size.width)!, height:(bounds?.size.height)! + 20)
        visualEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        visualEffectView.isUserInteractionEnabled = false
        visualEffectView.tag = 12
        //here you can choose one
        //        view.insertSubview(visualEffectView, at: 0)
        self.navigationController?.navigationBar.addSubview(visualEffectView)
        // Or
        /*
         If you find that after adding blur effect on navigationBar, navigation buttons are not visible then add below line after adding blurView code.
         */
        self.navigationController?.navigationBar.sendSubview(toBack: visualEffectView)
        
        // Here you can add visual effects to any UIView control.
        // Replace custom view with navigation bar in above code to add effects to custom view.
    }
}

extension UIScrollView {
    func setContentInsetAndScrollIndicatorInsets(edgeInsets: UIEdgeInsets) {
        self.contentInset = edgeInsets
        self.scrollIndicatorInsets = edgeInsets
    }
}


extension String {
    func index(from: Int) -> Index {
        return self.index(startIndex, offsetBy: from)
    }
    
    func substring(from: Int) -> String {
        let fromIndex = index(from: from)
        return substring(from: fromIndex)
    }
    
    func substring(to: Int) -> String {
        let toIndex = index(from: to)
        return substring(to: toIndex)
    }
    
    func substring(with r: Range<Int>) -> String {
        let startIndex = index(from: r.lowerBound)
        let endIndex = index(from: r.upperBound)
        return substring(with: startIndex..<endIndex)
    }
}
