import { Component, OnInit } from '@angular/core';

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
   * Third digit in the 404 text.
   */
  thirdDigit: string;

  /**
   * Second digit in the 404 text.
   */
  secondDigit: string;

  /**
   * First digit in the 404 text.
   */
  firstDigit: string;

  /**
   * Delay for generating the random numbers in every digit in the 404 text.
   * @type {number}
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
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.initialize404();
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
