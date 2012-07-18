import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.io.*;
public class SWTWebKitBrowser {

	static final String BASE_APPNAME = "SWTWebKitBrowser";
	public static String HOME;
	public static String accessToken="";
	static final String BLANK_PAGE_URL = "about:blank";
	static final String HOME_URL_PROPERTY="webkit4swt.home";
	public static String DEFAULT_HOME_URL="index.html";
	public static String LOGGEDIN_HOME_URL="homepage.html";
	public static String TAG_URL="addtag.jsp";
	public static String RECOMMENDATION_URL="html/rec.jsp";
	public static String CHECK_URL="check.jsp";
	public static String HELP_URL="help.html";
	public static boolean loggedin=false;
	static final String ELLIPSIS = "...";
	static final int MAX_TAB_LABEL_LENGTH = 28;

	public static Image newTabImg = new Image(Display.getDefault(),SWTWebKitBrowser.class.getResourceAsStream("images/newtab.png"));
	public static Image backImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/back.png"));
	public static Image fwdImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/forward.png"));	
	public static Image reloadImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/reload.png"));
	public static Image starImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/starred.png"));
	public static Image goImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/go.png"));	
	public static Image stopImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/stop.png"));	
	public static Image menuImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/notify.png"));
	public static Image homeImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/inbox.png"));
	public static Image tagImg = new Image(Display.getCurrent(),SWTWebKitBrowser.class.getResourceAsStream("images/tag.png"));
	
	Display display = Display.getCurrent();
	Shell shell;

	CTabFolder folder;
	CTabItem newTabItem;

	public static void main(String[] args) {

		try{
		System.getProperties().load(new FileInputStream("config.ini"));
		HOME=System.getProperty("home");
		DEFAULT_HOME_URL=HOME+DEFAULT_HOME_URL;
		LOGGEDIN_HOME_URL=HOME+LOGGEDIN_HOME_URL;
		TAG_URL=HOME+TAG_URL;
		RECOMMENDATION_URL=HOME+RECOMMENDATION_URL;
		CHECK_URL=HOME+CHECK_URL;
		HELP_URL=HOME+HELP_URL;
		SWTWebKitBrowser app = new SWTWebKitBrowser();
		app.run();
		}catch(Exception e){e.printStackTrace();}
	}

	public SWTWebKitBrowser() {
	}

	public void run() {

		createUI();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public void createUI() {
		display = Display.getDefault();
		shell = new Shell(display);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		shell.setLayout(layout);
		shell.setMaximized(true);
		createFolder(shell);
	}

	public void createFolder(Composite parent) {

		folder = new CTabFolder(parent, SWT.NONE);
		folder.setSimple(false);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginLeft = 0;
		folder.setLayout(layout);

		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		folder.setLayoutData(gd);

		createBrowserPage(folder, 0);
		newTabItem = createNewPage(folder);
	}
	
	BrowserPage createBrowserPage(CTabFolder folder, int pageIdx) {
		BrowserPage page = new BrowserPage(folder,pageIdx);
		return page;
	}


	CTabItem createNewPage(final CTabFolder folder) {

		final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
		tabItem.setShowClose(false);
		tabItem.setImage(newTabImg);

		folder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!(e.getSource() instanceof CTabFolder))
					return;

				final CTabFolder folder = (CTabFolder) e.getSource();
				final CTabItem item = (CTabItem) e.item;

				if (item.equals(newTabItem)) {
					int pageCnt = folder.getItemCount();
					int insertIdx = pageCnt - 1;
					final CTabItem newPage = 
							createBrowserPage(folder,insertIdx).getTabItem();

					folder.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							folder.setSelection(newPage);
							folder.showSelection();
						}
					});
				}

				System.out.println(e);
			}

		});

		folder.addCTabFolder2Listener(new CTabFolder2Adapter() {

			public void close(CTabFolderEvent event) {
				if (event.item.equals(tabItem)) {
					event.doit = false;
				}
			}
		});
		return tabItem;
	}

}
