import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {

  private endpointPrefix = '';

  setEndpoint(endpoint: string): void {
    console.log(`[AppConfigService.setEndpoint] endpoint=${endpoint}`);
    this.endpointPrefix = endpoint;
  }

  getEndpointFor(api: string, microservice?: string): string {
    if (microservice) {
      return `${this.endpointPrefix}services/${microservice}/${api}`;
    }
    return `${this.endpointPrefix}${api}`;
  }
}
