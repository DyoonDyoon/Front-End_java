package controller;

import view.DetailView.DetailViewType;

public interface ReloadListener {
	public void needsReloadData(DetailViewType type);
}