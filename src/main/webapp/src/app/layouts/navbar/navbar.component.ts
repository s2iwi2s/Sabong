import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  isNavbarCollapsed = true;
  version = '';
  env = '';

  constructor(private router: Router) {
    this.env = environment.env;
  }
  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }
  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  isAuthenticated() {
    return true;
  }

  logout() {

  }

  getImageUrl() {
    return '';
  }

  login() {
    this.router.navigate(['/login']);
  }
}
