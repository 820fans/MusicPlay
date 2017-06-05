package com.mp.music.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;


public class FragmentController {

	private int containerId;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();

	public FragmentController(FragmentActivity activity, int containerId) {
		this.containerId = containerId;
		fm = activity.getSupportFragmentManager();

		initFragment();
	}

	private void initFragment() {


		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new SearchFragment());
		fragments.add(new User2Fragment());

		ft= fm.beginTransaction();
		for(int i=0;i<fragments.size();i++) {
			ft.add(containerId,fragments.get(i));
		}
		
		ft.commit();
	}

	public void showFragment(int position) {
		
		hideFragments();
		
		Fragment f=fragments.get(position);
		if(position==0){
			if(f==null){
				fragments.add(position,new HomeFragment());
				ft.add(containerId,fragments.get(position));
			}
			else{
				ft.show(f);
			}
		}else if(position==1){
			if(f==null){
				fragments.add(position,new MessageFragment());
				ft.add(containerId,fragments.get(position));
			}
			else{
				ft.show(f);
			}
		}else if(position==2){
			if(f==null){
				fragments.add(position,new SearchFragment());
				ft.add(containerId,fragments.get(position));
			}
			else{
				ft.show(f);
			}
		}else if(position==3){
			if(f==null){
				fragments.add(position,new User2Fragment());
				ft.add(containerId,fragments.get(position));
			}
			else{
				ft.show(f);
			}
		}
		
		ft.commit();
	}
	
	//隐藏所有的Fragment,避免fragment混乱  
	public void hideFragments() {
		ft= fm.beginTransaction();
		for(int i=0;i<fragments.size();i++) {
			ft.hide(fragments.get(i));
		}
	}
	
}