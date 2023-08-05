import {Component, isDevMode, OnInit} from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  isDevMode = true;

  ngOnInit(): void {
    this.isDevMode = isDevMode();
  }

}
