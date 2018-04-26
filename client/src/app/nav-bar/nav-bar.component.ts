import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'gms-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  isCollapsed : boolean = true;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  isLinkActive(link: string): boolean {
    return this.router.isActive(link, true);
  }

}
