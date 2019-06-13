import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageNotFoundService } from '../core/navigation/page-not-found.service';

/**
 * Component for generating a (404) not found page. This is used when the user requests a path which is not defined in the routing.
 */
@Component({
  selector: 'gms-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})
export class PageNotFoundComponent implements OnInit {

  /**
   * First digit in the 404 text.
   * @type {string}
   */
  firstDigit: string;

  /**
   * Second digit in the 404 text.
   * @type {string}
   */
  secondDigit: string;

  /**
   * Third digit in the 404 text.
   * @type {string}
   */
  thirdDigit: string;

  /**
   * Delay for generating the random numbers in every digit in the 404 text.
   */
  private time = 30;

  /**
   * Generates a random number as string.
   * @returns {string}
   */
  private static randomNumber(): string {
    return (Math.floor(Math.random() * 9) + 1).toString();
  }

  /**
   * Component constructor
   */
  constructor(private router: Router, private pageNotFoundService: PageNotFoundService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    if (this.pageNotFoundService.wasNotFound(this.router.url)) {
      this.initialize404();
    } else {
      this.pageNotFoundService.addUrl(this.router.url);
      const i = this.router.url.lastIndexOf('/');
      if (i !== -1) {
        const sub = this.router.url.substring(0, i);
        if (sub === '') {
          this.initialize404();
        } else {
          this.router.navigateByUrl(`/${sub}`);
        }
      } else {
        this.router.navigateByUrl('/');
      }
    }
  }

  /**
   * Initializes the simulation of random numbers in order to show finally a 404
   */
  private initialize404(): void {
    let i = 0;
    const first4 = setInterval(() => {
      if (i++ > 40) {
        clearInterval(first4);
        this.firstDigit = '4';
      } else {
        this.firstDigit = PageNotFoundComponent.randomNumber();
      }
    }, this.time);
    const second4 = setInterval(() => {
      if (i++ > 80) {
        clearInterval(second4);
        this.secondDigit = '0';
      } else {
        this.secondDigit = PageNotFoundComponent.randomNumber();
      }
    }, this.time);
    const third4 = setInterval(() => {
      if (i++ > 100) {
        clearInterval(third4);
        this.thirdDigit = '4';
      } else {
        this.thirdDigit = PageNotFoundComponent.randomNumber();
      }
    }, this.time);
  }
}
