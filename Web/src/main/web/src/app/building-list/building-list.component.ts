import { Component, OnInit } from '@angular/core';
import {BuildingService} from '../building/building.service'
import {Building} from "../building";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-building-list',
  templateUrl: './building-list.component.html',
  styleUrls: ['./building-list.component.css']
})
export class BuildingListComponent implements OnInit {
  buildings:Array<any>;

  constructor(private buildingService: BuildingService, private route: ActivatedRoute,
  private router: Router) { }

  ngOnInit() {
    this.getAllBuildings();
  }

  getAllBuildings(){
    this.buildingService.getAll().subscribe(data => {
      this.buildings = data;
    });
  }

  deleteBuilding(building:Building){
    let result = confirm("Do you want to delete building with name " + building.name + " and address " + building.address + "?");
    if(result){
      this.buildingService.deleteBuilding(building).subscribe(data => {
        this.buildings.splice(this.buildings.indexOf(building),1);
      })
    }
  }

  onAddBuilding() {
    this.router.navigate(['building/create'],{relativeTo: this.route});
  }
}
