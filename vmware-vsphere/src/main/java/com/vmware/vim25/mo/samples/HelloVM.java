package com.vmware.vim25.mo.samples;

import java.net.URL;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class HelloVM {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		ServiceInstance si = new ServiceInstance(new URL("https://localhost.locallan/sdk"), "vc3@vsphere.local",
				"Abcd1234##", true);
		long end = System.currentTimeMillis();
		System.out.println("time taken:" + (end - start));
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		if (mes == null || mes.length == 0) {
			return;
		}
		for (int i = 0; i < mes.length; i++) {
			VirtualMachine vm = (VirtualMachine) mes[i];

			VirtualMachineConfigInfo vminfo = vm.getConfig();
			VirtualMachineCapability vmc = vm.getCapability();

			vm.getResourcePool();
			System.out.println("VM Name: " + vm.getName());
			System.out.println("GuestOS: " + vminfo.getGuestFullName());
			System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
			System.out.println("");
		}
		si.getServerConnection().logout();
	}

}
