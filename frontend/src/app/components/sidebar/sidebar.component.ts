import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GroupComponent } from '../group/group.component';
import { ProfileComponent } from '../profile/profile.component';

@Component({
	selector: 'app-sidebar',
	standalone: true,
	imports: [CommonModule, GroupComponent, ProfileComponent],
	templateUrl: './sidebar.component.html',
	styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
}