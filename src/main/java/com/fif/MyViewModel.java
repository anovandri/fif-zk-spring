package com.fif;

import com.fif.acl.services.impl.UserServiceImpl;
import com.fif.entity.Log;
import com.fif.services.MyService;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MyViewModel {

	@WireVariable
	private MyService myService;

	@WireVariable
	UserServiceImpl userService;

	private ListModelList<Log> logListModel;
	private String message;

	private boolean addButtonVisible = true;
	private boolean deleteButtonVisible = true;

	@Init
	public void init() {
		List<Log> logList = myService.getLogs();
		logListModel = new ListModelList<Log>(logList);
	}

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Set<String> roles = userService.getLoggedInUserRoles();
		if (roles.contains("ROLE_ADMIN")) {
			addButtonVisible = true;
			deleteButtonVisible = true;
		} else {
			addButtonVisible = true;
			deleteButtonVisible = false;
		}
	}

	public ListModel<Log> getLogListModel() {
		return logListModel;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Command
	public void addLog() {
		if(Strings.isBlank(message)) {
			return;
		}
		Log log = new Log(message);
		log = myService.addLog(log);
		logListModel.add(log);
	}

	@Command
	public void deleteLog(@BindingParam("log") Log log) {
		myService.deleteLog(log);
		logListModel.remove(log);
	}

	public boolean isAddButtonVisible() {
		return addButtonVisible;
	}

	public void setAddButtonVisible(boolean addButtonVisible) {
		this.addButtonVisible = addButtonVisible;
	}

	public boolean isDeleteButtonVisible() {
		return deleteButtonVisible;
	}

	public void setDeleteButtonVisible(boolean deleteButtonVisible) {
		this.deleteButtonVisible = deleteButtonVisible;
	}
}
