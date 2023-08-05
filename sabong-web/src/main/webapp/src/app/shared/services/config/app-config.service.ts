import { Injectable } from '@angular/core';
import {APP_NAME, APP_VERSION} from "../../app.constants";

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private ENV = {};

  setENV(env: any): void {
    console.log(`[AppConfigService.setENV] env=`, env);
    this.ENV = env;
  }

  get(key: string) : any {
    // @ts-ignore
    return this.ENV? this.ENV[key] : '';
  }

  getEndpointFor(api: string, microservice?: string): string {
    let endpointPrefix = this.get("API_URL");
    if (microservice) {
      return `${endpointPrefix}services/${microservice}/${api}`;
    }
    return `${endpointPrefix}${api}`;
  }
}
