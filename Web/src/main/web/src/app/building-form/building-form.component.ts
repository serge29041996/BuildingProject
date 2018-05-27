import {Component, OnInit, ViewChild} from '@angular/core';
import {BuildingService} from '../building/building.service';
import {NgForm} from '@angular/forms';
import {Building} from "../building";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-building-form',
  templateUrl: './building-form.component.html',
  styleUrls: ['./building-form.component.css']
})
export class BuildingFormComponent implements OnInit {
  @ViewChild('f') signupForm: NgForm;
  name = '';
  address = '';
  numberResidents = '';
  image: string = null;
  typeImage: string = null;
  resultMessage = '';
  idUpdatedBuilding: number;
  isValidId: boolean = true;
  typePage: string = '';
  oldDataBuilding: Building = null;
  oldImage: string = null;
  oldTypeImage: string = null;
  styleForMessage = '';

  errors = [
    {
      field: 'name',
      message: ''
    },
    {
      field: 'address',
      message: ''
    },
    {
      field: 'numberResidents',
      message: ''
    },
    {
      field: 'commonErrors',
      message: ''
    }
  ];

  constructor(private buildingService: BuildingService, private route: ActivatedRoute,
  private router: Router) { }

  ngOnInit() {
    let numberParameters = this.route.snapshot.paramMap.keys.length;

    if(numberParameters === 1){
      let operation = this.route.snapshot.params['operation'];
      this.validateUrlParameters(operation, null);
    }
    else {
      let operation = this.route.snapshot.params['operation'];
      let idString = this.route.snapshot.params['id'];
      if(this.validateUrlParameters(operation, idString)) {
        this.typePage = 'update';
        this.buildingService.getBuilding(this.idUpdatedBuilding).subscribe(
          (data: Building) => {
            this.idUpdatedBuilding = data.id;
            this.name = data.name;
            this.address = data.address;
            this.numberResidents = '' + data.numberResidents;
            this.image = data.image;
            this.typeImage = data.typeImage;
            this.oldDataBuilding = data;
            this.oldImage = data.image;
            this.oldTypeImage = data.typeImage;
          },
          (error) => {
            this.resultMessage = 'Building with id ' + idString + ' is not exist';
            this.isValidId = false;
          }
        )
      }
    }
  }

  validateUrlParameters(operation: string, id: string): boolean {
    if(operation === 'create'){
      if(id !== null){
        this.router.navigate(['/not-found']);
      }
    } else if(operation === 'update'){
      if(this.isNumber(id)){
        this.idUpdatedBuilding = parseInt(id);
        this.isValidId = true;
        return true;
      }
      else{
        return false;
      }
    }
    else {
      this.router.navigate(['/not-found']);
    }
  }

  addImage($event): void{
    this.readImageAsBase64String($event.target);
  }

  readImageAsBase64String(inputValue: any) {
    let file: File = inputValue.files[0];
    let myReader: FileReader = new FileReader();

    myReader.onloadend = (e) => {
      let dataImage = myReader.result.split(',');
      this.typeImage = dataImage[0];
      this.image = dataImage[1];
    };
    myReader.readAsDataURL(file);
  }

  onBackList() {
    this.router.navigate(['']);
  }

  addBuilding(){
    if(this.typePage === '') {
      let numberResidents: number;
      let isHaveError = false;

      if (!this.isNumber(this.numberResidents)) {
        this.errors[2].message = 'Invalid value. Number of residents is a number';
        isHaveError = true;
      }
      else {
        numberResidents = parseInt(this.numberResidents);
        this.errors[2].message = '';
      }

      if(this.name !== '' && this.address !==''){
        this.errors[0].message = '';
        this.errors[1].message = '';
      }
      else{
        if(this.name === ''){
          this.errors[0].message = 'Don`t entered name of the building.';
          isHaveError = true;
        }
        if(this.address === ''){
          this.errors[1].message = 'Don`t entered address of the building.';
          isHaveError = true;
        }
      }

      if(!isHaveError){
        let building = new Building(this.name,this.address,0,numberResidents,this.image,this.typeImage);
        this.buildingService.addBuilding(building).subscribe(
          (data: any) => {
            this.name = '';
            this.address = '';
            this.numberResidents = '';
            this.image = null;
            this.typeImage = null;
            this.errors[3].message = '';
            this.resultMessage = 'The information about building has saved.';
            this.outputMessage();
            this.signupForm.resetForm();
          },
          (error) => {
            if(error.subErrors === null){
              this.errors[3].message = error.message;
              this.outputMessage();
            }
            else if(error.subErrors.length === 0){
              this.errors[3].message = error.message;
              this.outputMessage();
            }
            else{
              this.setErrorsOfResponse(error.subErrors);
            }
          }
        );
      }
    }
    else {
      if(!this.signupForm.touched) {
        this.errors[3].message = 'The information has not changed';
        this.outputMessage();
      } else {
        let numberUnits: number;
        let numberResidents: number;
        let isHaveError = false;

        if (!this.isNumber(this.numberResidents)) {
          this.errors[2].message = 'Invalid value. Number of residents is a number';
          isHaveError = true;
        }
        else {
          numberResidents = parseInt(this.numberResidents);
          this.errors[3].message = '';
        }

        if(this.name !== '' && this.address !==''){
          this.errors[0].message = '';
          this.errors[1].message = '';
        }
        else{
          if(this.name === ''){
            this.errors[0].message = 'Don`t entered name of the building.';
            isHaveError = true;
          }
          if(this.address === ''){
            this.errors[1].message = 'Don`t entered address of the building.';
            isHaveError = true;
          }
        }

        if(!isHaveError){
          let building = new Building(this.name,this.address,numberUnits,numberResidents,
                                      this.image,this.typeImage);
          if(!this.isEqualBuiding(building)){
            building.id = this.idUpdatedBuilding;
            this.buildingService.updateBuilding(building).subscribe(
              (data: any) => {
                this.errors[3].message = '';
                this.resultMessage = 'The information about building has updated.';
                this.outputMessage();
              },
              (error) => {
                console.log(error);
                if(error.subErrors === null){
                  this.errors[3].message = error.message;
                  this.outputMessage();
                }
                else if(error.subErrors.length === 0){
                  this.errors[3].message = error.message;
                  this.outputMessage();
                }
                else{
                  this.setErrorsOfResponse(error.subErrors);
                }
              }
            );
          } else {
            this.errors[4].message = 'The information has not changed';
            this.outputMessage();
          }
        }
      }
    }

  }

  isNumber(fieldValue: string): boolean {
    let value = parseInt(fieldValue);
    return !isNaN(value) && isFinite(value);
  }

  setErrorsOfResponse(errors: {field: string, message: string}[]) {
    for(let i=0; i< errors.length; i++){
      this.errors.find(
        x => x.field === errors[i].field
      ).message = errors[i].message;
    }
  }

  isEqualBuiding(newBuilding: Building) {
    let isEqualBuilding = true;
    if(this.oldDataBuilding.name !== newBuilding.name) {
      return false;
    }

    if(isEqualBuilding) {
      if(this.oldDataBuilding.address !== newBuilding.address) {
        return false;
      }
    }

    if(isEqualBuilding) {
      if(this.oldDataBuilding.numberUnits !== newBuilding.numberUnits) {
        return false;
      }
    }

    if(isEqualBuilding) {
      if(this.oldDataBuilding.numberResidents !== newBuilding.numberResidents) {
        return false;
      }
    }

    if(isEqualBuilding) {
      if(this.oldDataBuilding.image !== newBuilding.image) {
        return false;
      }
    }

    return isEqualBuilding;
  }

  outputMessage() {
    this.styleForMessage = 'output-message';
    setTimeout(
      () => this.styleForMessage = '',3000);
  }

}
