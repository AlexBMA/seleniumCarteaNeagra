import { Component, OnInit } from '@angular/core';
import { FileOperationServiceService } from 'src/app/services/file-operation-service.service';


@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  fileName = '';

  glowValue = 1.5;
  glowColor = '#000000';

  outlineValue = 1.5;
  outlineColor = '#000000';

  textSize = 54;
  textColor = '#ffffff';

  backgroundColor = '#092D26';

  linkText ="";
  searchText="";

  width = window.screen.width;
  height = window.screen.height;
  
  constructor(private  fileOperationServiceService: FileOperationServiceService) { }

  ngOnInit(): void {

  }

  onButtonSearch(){

    const formData = new FormData();
    this.addCommonElemets(formData);

    formData.append("url", this.searchText);
    
    this.fileOperationServiceService.sendSerachText(formData)
        .subscribe({
          complete:()=> console.log("done send to search"),
          next: (resp) =>{
              this.processResult(resp,this.searchText);
            }
          });
  }

  onButtonLink(){
     
      const formData = new FormData();
      this.addCommonElemets(formData);

      formData.append("url", this.linkText);

      let split = this.linkText.split("/");
      let name = split[split.length-1].replace("-"," ");
        
      this.fileOperationServiceService.sendLink(formData)
          .subscribe({
            complete:()=> console.log("done send to link"),
            next: (resp)=> {
              this.processResult(resp,name);
            }
          });
  }

  private addCommonElemets(formData: FormData) {

    let options = {
      width: this.width,
      height: this.height,
      textSize: this.textSize,
      textColor: this.textColor,
      glowValue: this.glowValue,
      glowColor: this.glowColor,
      outlineValue: this.outlineValue,
      outlineColor: this.outlineColor,
      backgroundColor: this.backgroundColor
    }

    formData.append("options",JSON.stringify(options));

    
  }

  onFileSelected(event: any ) { 
    const file:File = event.target.files[0];

    if (file) {
        
        this.fileName = file.name;
        const formData = new FormData();
        this.addCommonElemets(formData);
        formData.append("file", file);
        
        this.fileOperationServiceService
            .sendFile(formData).subscribe({
              complete:()=> console.log("done"),
            
              next: (resp)=> {
                this.processResult(resp,this.fileName);
              }
            })
    

    }
}


  private processResult(resp: any,pptFileName:string) {
    console.log(resp);

    const a = document.createElement('a');
    const blob = new Blob([resp], { type: 'application/vnd.openxmlformats-officedocument.presentationml.presentation' });
    const objectUrl = URL.createObjectURL(blob);
    a.href = objectUrl;
    a.download = `${pptFileName}.pptx`;
    a.click();
    URL.revokeObjectURL(objectUrl);
    a.remove();
  }
}
