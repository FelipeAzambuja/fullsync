/*
 * Created on Nov 18, 2004
 */
package net.sourceforge.fullsync.remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import net.sourceforge.fullsync.Profile;
import net.sourceforge.fullsync.TaskTree;

/**
 * @author Michele Aiello
 */
public class RemoteManager {

	private RemoteInterface remoteInterface;

	public RemoteManager(String host, int port, String password) 
		throws MalformedURLException, RemoteException, NotBoundException 
	{
		remoteInterface = (RemoteInterface) Naming.lookup("rmi://"+host+":"+port+"/FullSync");
		if (!remoteInterface.checkPassword(password)) {
			throw new RemoteException("Wrong password");
		}
	}
	
	public Profile getProfile(String name) {
		try {
			Profile remoteprofile = remoteInterface.getProfile(name);
			return remoteprofile;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Profile[] getProfiles() {
		try {
			Profile[] remoteprofiles = remoteInterface.getProfiles();
			return remoteprofiles;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void runProfile(String name) throws RemoteException {
		remoteInterface.runProfile(name);
	}

	public TaskTree executeProfile(String name) throws RemoteException {
		return remoteInterface.executeProfile(name);
	}
	
	public void performActions(TaskTree taskTree) throws RemoteException {
		remoteInterface.performActions(taskTree);
	}

	public void save(Profile[] profiles) {
		try {
			remoteInterface.save(profiles);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}