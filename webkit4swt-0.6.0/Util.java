import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Util {

	public static void updateTabItemTitle(final CTabItem tabItem, final String label) {
		if (label == null || label.trim().isEmpty())
			return; // do nothing and keep old label
		
		runAsync( new Runnable() {
			@Override
			public void run() {
				tabItem.setText(shortenString(label, SWTWebKitBrowser.MAX_TAB_LABEL_LENGTH));
			}
		});
	}

	public static String shortenString(String string, int length) {
		String result;

		result = string != null ? string.trim() : "";
		if (result.length() > length) {
			result = result.substring(0, Math.max(length - 4, 1)) + SWTWebKitBrowser.ELLIPSIS;
		}
		return result;
	}

	public static void runAsync(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	public String createNewTabContent() {
		return "<html><body><p>hello world</p></body></html>";
	}

	public static String getHomeURL() {
		String homeURL = System
				.getProperty(SWTWebKitBrowser.HOME_URL_PROPERTY, SWTWebKitBrowser.DEFAULT_HOME_URL);
		return homeURL;
	}
	

	public static void updateWindowTitle(final Shell shell, String pageTitle) {
		if (shell == null)
			return;

		String newTitle = pageTitle != null ? pageTitle.trim() : "";
		newTitle = shortenString(newTitle, SWTWebKitBrowser.MAX_TAB_LABEL_LENGTH);
		newTitle = newTitle.isEmpty() ? SWTWebKitBrowser.BASE_APPNAME : newTitle + "  -  "
				+ SWTWebKitBrowser.BASE_APPNAME;

		final String finalNewTitle = newTitle;
		runAsync( new Runnable() {
			@Override
			public void run() {
				shell.setText(finalNewTitle);
			}
		});		
	}
	
	public static void updateAddressText(final Text addressBar, final String url) {
		runAsync( new Runnable() {
			@Override
			public void run() {
				addressBar.setText(url);
			}
		});	
	}
}
