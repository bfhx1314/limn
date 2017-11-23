//package com.limn.android.tool;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.NullProgressMonitor;
//
//import com.android.ddmlib.CollectingOutputReceiver;
//import com.android.ddmlib.IDevice;
//import com.android.ddmlib.RawImage;
//import com.android.ddmlib.SyncService;
//import com.limn.tool.common.Common;
//import com.limn.tool.common.Print;
//
//public class AndroidTool {
//
//	public static final int UIAUTOMATOR_MIN_API_LEVEL = 16;
//
//	private static final String UIAUTOMATOR = "/system/bin/uiautomator"; //$NON-NLS-1$
//	private static final String UIAUTOMATOR_DUMP_COMMAND = "dump"; //$NON-NLS-1$
//	private static final String UIDUMP_DEVICE_PATH = "/data/local/tmp/uidump.xml"; //$NON-NLS-1$
//	private static final int XML_CAPTURE_TIMEOUT_SEC = 40;
//
//	private static boolean supportsUiAutomator(IDevice device) {
//		String apiLevelString = device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
//		int apiLevel;
//		try {
//			apiLevel = Integer.parseInt(apiLevelString);
//		} catch (NumberFormatException e) {
//			apiLevel = UIAUTOMATOR_MIN_API_LEVEL;
//		}
//
//		return apiLevel >= UIAUTOMATOR_MIN_API_LEVEL;
//	}
//
//	public static UiAutomatorResult takeSnapshot(IDevice device) {
//		IProgressMonitor monitor = new NullProgressMonitor();
//
//		monitor.subTask("Checking if device support UI Automator");
//		if (!supportsUiAutomator(device)) {
//			String msg = "UI Automator requires a device with API Level " + UIAUTOMATOR_MIN_API_LEVEL;
//			// throw new UiAutomatorException(msg, null);
//		}
//
//		monitor.subTask("Creating temporary files for uiautomator results.");
//		File tmpDir = null;
//		File xmlDumpFile = null;
//		File screenshotFile = null;
//		try {
//			tmpDir = File.createTempFile("uiautomatorviewer_", "");
//			tmpDir.delete();
//			if (!tmpDir.mkdirs())
//				throw new IOException("Failed to mkdir");
//			xmlDumpFile = File.createTempFile("dump_", ".uix", tmpDir);
//			screenshotFile = File.createTempFile("screenshot_", ".png", tmpDir);
//		} catch (Exception e) {
//			String msg = "Error while creating temporary file to save snapshot: " + e.getMessage();
//			// throw new UiAutomatorException(msg, e);
//		}
//
//		tmpDir.deleteOnExit();
//		xmlDumpFile.deleteOnExit();
//		screenshotFile.deleteOnExit();
//
//		monitor.subTask("Obtaining UI hierarchy");
//		try {
//			// UiAutomatorHelper.getUiHierarchyFile(device, xmlDumpFile,
//			// monitor, compressed);
//		} catch (Exception e) {
//			String msg = "Error while obtaining UI hierarchy XML file: " + e.getMessage();
//			// throw new UiAutomatorException(msg, e);
//		}
//
//		// UiAutomatorModel model;
//		try {
//			// model = new UiAutomatorModel(xmlDumpFile);
//		} catch (Exception e) {
//			String msg = "Error while parsing UI hierarchy XML file: " + e.getMessage();
//			// throw new UiAutomatorException(msg, e);
//		}
//
//		monitor.subTask("Obtaining device screenshot");
//		RawImage rawImage;
//		try {
//			rawImage = device.getScreenshot();
//			rawImage.getRotated();
//
//			new test(rawImage);
//		} catch (Exception e) {
//			String msg = "Error taking device screenshot: " + e.getMessage();
//			// throw new UiAutomatorException(msg, e);
//		}
//
//		// rotate the screen shot per device rotation
////		BasicTreeNode root = model.getXmlRootNode();
////		if (root instanceof RootWindowNode) {
////			for (int i = 0; i < ((RootWindowNode) root).getRotation(); i++) {
////				rawImage = rawImage.getRotated();
////			}
////		}
//
//
////		PaletteData palette = new PaletteData(rawImage.getRedMask(), rawImage.getGreenMask(), rawImage.getBlueMask());
////		ImageData imageData = new ImageData(rawImage.width, rawImage.height, rawImage.bpp, palette, 1, rawImage.data);
////		ImageLoader loader = new ImageLoader();
////		loader.data = new ImageData[] { imageData };
////		loader.save(screenshotFile.getAbsolutePath(), SWT.IMAGE_PNG);
////		Image screenshot = new Image(Display.getDefault(), imageData);
//
//		// return new UiAutomatorResult(xmlDumpFile, model, screenshot);
//		return null;
//	}
//
//	 @SuppressWarnings("deprecation")
//	public static void getUiHierarchyFile(IDevice device, File dst,
//	            IProgressMonitor monitor, boolean compressed) {
//	        if (monitor == null) {
//	            monitor = new NullProgressMonitor();
//	        }
//
//	        monitor.subTask("Deleting old UI XML snapshot ...");
//	        String command = "rm " + UIDUMP_DEVICE_PATH;
//
//	        try {
//	            CountDownLatch commandCompleteLatch = new CountDownLatch(1);
//	            device.executeShellCommand(command,
//	                    new CollectingOutputReceiver(commandCompleteLatch));
//	            commandCompleteLatch.await(5, TimeUnit.SECONDS);
//	        } catch (Exception e1) {
//	            // ignore exceptions while deleting stale files
//	        }
//
//	        monitor.subTask("Taking UI XML snapshot...");
//
//	            command = String.format("%s %s %s", UIAUTOMATOR,
//	                    UIAUTOMATOR_DUMP_COMMAND,
//	                    UIDUMP_DEVICE_PATH);
//
//	        CountDownLatch commandCompleteLatch = new CountDownLatch(1);
//
//	        try {
//	        	device.executeShellCommand(command,  new CollectingOutputReceiver(commandCompleteLatch));
////	            device.executeShellCommand(
////	                    command,
////	                    new CollectingOutputReceiver(commandCompleteLatch),
////	                    XML_CAPTURE_TIMEOUT_SEC * 1000);
//	            commandCompleteLatch.await(XML_CAPTURE_TIMEOUT_SEC, TimeUnit.SECONDS);
//
//	            monitor.subTask("Pull UI XML snapshot from device...");
//	            device.getSyncService().pullFile(UIDUMP_DEVICE_PATH,
//	                    dst.getAbsolutePath(), SyncService.getNullProgressMonitor());
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//	    }
//
//
//	public static class UiAutomatorResult {
//		public final File uiHierarchy;
//		// public final UiAutomatorModel model;
//
//		public UiAutomatorResult(File uiXml) {
//			uiHierarchy = uiXml;
//			// model = m;
//
//		}
//	}
//
//	private static IDevice pickDevice() {
//
//		List<IDevice> devices = DebugBridge.getDevices();
//
//		if (devices.size() == 0) {
//			Print.log("error", 2);
//
//			return null;
//		} else if (devices.size() == 1) {
//			// return devices.get(0);
//		}
//
//		Print.log(devices.size() + "", 2);
//
//		return devices.get(0);
//
//		// if (devices.size() == 0) {
//		// MessageDialog.openError(mViewer.getShell(),
//		// "Error obtaining Device Screenshot",
//		// "No Android devices were detected by adb.");
//		// return null;
//		// } else if (devices.size() == 1) {
//		// return devices.get(0);
//		// } else {
//		// DevicePickerDialog dlg = new DevicePickerDialog(mViewer.getShell(),
//		// devices);
//		// if (dlg.open() != Window.OK) {
//		// return null;
//		// }
//		// return dlg.getSelectedDevice();
//	}
//
//	public static void main(String[] args) {
//		System.setProperty("com.android.uiautomator.bindir", "/Users/limengnan/Documents/tool/sdk/tools");
//
//		DebugBridge.init();
//		// 初始化需要时间
//		Common.wait(1000);
//		List<IDevice> devices = DebugBridge.getDevices();
//		AndroidTool.getUiHierarchyFile(devices.get(0), new File("/Users/limengnan/Downloads/uml.xml"), null, true);
////		AndroidTool.takeSnapshot(devices.get(0));
//
//	}
//
//}
