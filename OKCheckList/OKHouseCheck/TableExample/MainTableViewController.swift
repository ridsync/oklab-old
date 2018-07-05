//
//  DataTableViewController.swift
//  TableExample
//
//  Created by Ralf Ebert on 19/09/16.
//  Copyright © 2016 Example. All rights reserved.
//

import UIKit
import CoreData

class MainTableViewController: UITableViewController {
    
    var people: [NSManagedObject] = []
    
    var moContext : NSManagedObjectContext!
    
    var houseList: [House] = []

    
    // MARK: - UIViewController LifeCycle
    override func viewDidLoad() {
        //        setNavigationBarExtendStyle()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        //        setNavigationBarEffectViewSend()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        //        setNavigationBarDefatulStyle()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if #available(iOS 10.0, *) {
            moContext = (UIApplication.shared.delegate as! AppDelegate).persistentContainer.viewContext
        } else if #available(iOS 9.0, *){
            moContext = (UIApplication.shared.delegate as! AppDelegate).managedObjectContext
        }

        getData()
        tableView.reloadData()
    }
    
    // MARK: - UITableViewDataSource
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return houseList.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "houseCell", for: indexPath) as! BranchCustomCell

        let house = houseList[indexPath.row]
        cell.tv_userName?.text = house.houseName
        cell.address?.text = "주소: \(house.address!)"
        cell.location?.text = "위치: \(house.location!)"
        cell.price?.text = "가격: \(house.price!)"
        cell.date?.text = "이사예정날짜: \(house.date!)"
        cell.totalScore?.text = "총점: \(house.totalScore)"
        return cell
    }
    
    // MARK: functions
    
    func getData() {
        do {
            let fec:NSFetchRequest = House.fetchRequest()
            let sortDescriptor = NSSortDescriptor(key: "regDate", ascending: false)
            fec.sortDescriptors = [sortDescriptor]
            houseList = try moContext.fetch(fec)
        }
        catch {
            print("Fetching Failed")
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "segCellSelect" {
            if let dest = segue.destination as? DetailViewController {
                if let selectedIndex = self.tableView.indexPathForSelectedRow?.row {
                    dest.selectHouse = self.houseList[selectedIndex] as House
                }
            }
        }
    }

    
}
