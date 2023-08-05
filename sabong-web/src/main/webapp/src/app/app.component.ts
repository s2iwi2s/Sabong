import {Component, OnInit} from '@angular/core';
import {ActivatedRouteSnapshot, NavigationEnd, Router} from "@angular/router";
import {environment} from "../environments/environment";
import {AppConfigService} from "./shared/services/config/app-config.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'SabongApp';

  constructor(private router: Router, private appConfigService: AppConfigService) {
    this.appConfigService.setENV(environment);

    console.log(`Actuator Endpoint: `, this.appConfigService.getEndpointFor("/actuator"))
  }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });
  }
  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);

    if (!pageTitle) {
      pageTitle = this.title;
    }
    this.title = pageTitle;
  }
  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.title ?? '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }
}
