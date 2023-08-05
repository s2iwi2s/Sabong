import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {AppConfigService} from "../../shared/services/config/app-config.service";
import {APP_NAME, APP_VERSION} from "../../shared/app.constants";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  isNavbarCollapsed = true;
  proc: any
  appVersion = APP_VERSION;
  appName = APP_NAME;
  env = '';

  constructor(private router: Router, private appConfigService: AppConfigService) {
    this.env = appConfigService.get("env");
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
