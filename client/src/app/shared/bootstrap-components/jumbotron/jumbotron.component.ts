import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'gms-jumbotron',
  templateUrl: './jumbotron.component.html',
  styleUrls: ['./jumbotron.component.scss']
})
export class GmsJumbotronComponent implements OnInit {

  @Input() header: string = null;
  @Input() text: string = null;
  @Input() link: string = null;
  @Input() linkText: string = null;
  @Input() sizeClass: string = 'btn-lg';
  @Input() typeClass: string = 'btn-primary';

  constructor() { }

  ngOnInit() {
  }

}
