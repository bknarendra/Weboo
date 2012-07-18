import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.net.*;
import java.awt.datatransfer.*;
import java.io.*;

import com.genuitec.blinki.webkit.swt.WebKitBrowser;
import com.genuitec.blinki.webkit.swt.WebKitBrowserAdapter;
import com.restfb.*;
import com.restfb.types.FacebookType;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.WindowEvent;

class BrowserPage {
	CTabFolder tabFolder;
	CTabItem tabItem;
	Button backBtn;
	Button fwdBtn;
	Button reloadBtn;
	Button starBtn;
	Button tagBtn;
	Text addressTxt;
	Button goBtn;
	Button homeBtn;
	Button menuBtn;
	int x,y;
	String prevurl="";
	Display display;
//	Shell shell;
	public static boolean isChar(char ch){
		if((ch>='A'&&ch<='Z')||(ch>='a'&&ch<='z')) return true;
		return false;
	}
	public static String awesm(String q){
		int i=0;
		if(q.startsWith("http://")||q.startsWith("https://")) return q;
		else if(q.startsWith("@")){
			String all[]=q.split(" ");
			if(all.length==1){
				if(q.equals("@twtr")) return "http://twitter.com/";
			else if(q.equals("@amzn")) return "http://www.amazon.com/";
			else if(q.equals("@ln")) return "http://www.linkedin.com/";
			else if(q.equals("@wiki")) return "http://en.wikipedia.org/";
			else if(q.equals("@imdb")) return "http://www.imdb.com/";
			else if(q.equals("@ebay")) return "http://www.ebay.com/";
			else if(q.equals("@flkr")) return "http://www.flickr.com/";
			else if(q.equals("@yt")) return "http://www.youtube.com/";
			else if(q.equals("@gb")) return "http://www.google.com/";
			else if(q.equals("@gs")) return "http://www.google.com/";
			else if(q.equals("@gi")) return "http://www.google.com/";
			else if(q.equals("@gm")) return "http://maps.google.com/";
			else if(q.equals("@gn")) return "http://www.google.com/";
			else if(q.equals("@fbg")) return "http://teckzone.in/fbinstant/";
			else if(q.equals("@fbml")) return "http://www.facebook.com/";
			else if(q.equals("@fbln")) return "http://www.facebook.com/";
			}
			else{
			for(i=0;isChar(q.charAt(++i)););
			String first=q.substring(0,i);
			String query=q.substring(i+1);
			if(first.equals("@twtr")) return "http://twitter.com/#!/search/"+query;
			else if(first.equals("@amzn")) return "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords="+query;
			else if(first.equals("@ln")) {String arr[]=query.split(" ");return "http://www.linkedin.com/pub/dir/?first="+arr[0]+"&last="+arr[1]+"&search=Search&searchType=fps";}
			else if(first.equals("@wiki")) return "http://en.wikipedia.org/wiki/"+query;
			else if(first.equals("@imdb")) return "http://www.imdb.com/find?q="+query+"&s=all";
			else if(first.equals("@ebay")) return "http://www.ebay.com/sch/i.html?_nkw="+query;
			else if(first.equals("@flkr")) return "http://www.flickr.com/search/?q="+query+"&f=hp";
			else if(first.equals("@yt")) return "http://www.youtube.com/results?search_query="+query;
			else if(first.equals("@gb")) return "http://www.google.com/search?q="+query+"&tbm=bks";
			else if(first.equals("@gs")) return "http://www.google.com/search?q="+query+"&tbm=shop";
			else if(first.equals("@gi")) return "http://www.google.com/search?q="+query+"&tbm=isch";
			else if(first.equals("@gm")) return "http://maps.google.com/maps?q="+query;
			else if(first.equals("@gn")) return "http://www.google.com/search?q="+query+"&tbm=nws";
			else if(first.equals("@fbg")) return "http://teckzone.in/fbinstant/#"+query;
			else if(first.equals("@fbml")) return "http://www.facebook.com/"+query;
			else if(first.equals("@fbln")) return "http://www.facebook.com/search/results.php?q="+query;
			else if(first.equals("@fbus")){
				DefaultFacebookClient facebookClient=new DefaultFacebookClient(SWTWebKitBrowser.accessToken);
				FacebookType publishMessageResponse=facebookClient.publish("me/feed",FacebookType.class,Parameter.with("message",query));
				return "Status updated successfully........";
			}
			}
		}
		return "http://www.google.co.in/search?q="+q;
	}
	public BrowserPage(CTabFolder folder, int pageIdx){
		tabFolder = folder;
		createBrowserPage(folder, pageIdx);
	}
	
	public CTabItem createBrowserPage(final CTabFolder folder, int index) {
		
		tabItem = new CTabItem(folder, SWT.CLOSE, index);
		tabItem.setText("New Tab");

		Composite pageComposite = new Composite(folder, SWT.NONE);
		GridLayout layout=new GridLayout();
		layout.marginHeight=0;
		layout.marginWidth=0;
		layout.verticalSpacing=0;
		layout.horizontalSpacing=0;
		pageComposite.setLayout(layout);

		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		pageComposite.setLayoutData(gd);

		Composite ctrlBar = new Composite(pageComposite, SWT.NONE);
		layout = new GridLayout(9, false);
		layout.marginHeight = 0;
		layout.marginTop = 5;
		layout.marginBottom = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		ctrlBar.setLayout(layout);

		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		ctrlBar.setLayoutData(gd);

		Rectangle bounds = SWTWebKitBrowser.backImg.getBounds();

		backBtn = new Button(ctrlBar, SWT.PUSH);
		backBtn.setImage(SWTWebKitBrowser.backImg);
		backBtn.setSize(bounds.width, bounds.height);
		gd = new GridData(bounds.width, bounds.height);
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalIndent = 5;
		backBtn.setLayoutData(gd);
		backBtn.setEnabled(false);

		bounds = SWTWebKitBrowser.fwdImg.getBounds();

		fwdBtn = new Button(ctrlBar, SWT.PUSH);
		fwdBtn.setImage(SWTWebKitBrowser.fwdImg);
		fwdBtn.setSize(bounds.width, bounds.height);
		gd = new GridData(bounds.width, bounds.height);
		fwdBtn.setLayoutData(gd);
		fwdBtn.setEnabled(false);

		bounds = SWTWebKitBrowser.reloadImg.getBounds();

		reloadBtn = new Button(ctrlBar, SWT.PUSH);
		reloadBtn.setImage(SWTWebKitBrowser.reloadImg);
		reloadBtn.setSize(bounds.width, bounds.height);
		gd = new GridData(bounds.width, bounds.height);
		gd.horizontalIndent = 10;
		reloadBtn.setLayoutData(gd);

		addressTxt = new Text(ctrlBar, SWT.SINGLE | SWT.BORDER | SWT.FLAT);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = bounds.height - 8;
		addressTxt.setLayoutData(gd);
		Font font = new Font(display, "Arial", 12, SWT.NORMAL);
		addressTxt.setFont(font);

		bounds = SWTWebKitBrowser.goImg.getBounds();

		goBtn = new Button(ctrlBar, SWT.PUSH);
		goBtn.setImage(SWTWebKitBrowser.goImg);
		goBtn.setSize(bounds.width, bounds.height);
		gd = new GridData(bounds.width, bounds.height);
		goBtn.setLayoutData(gd);

		bounds = SWTWebKitBrowser.starImg.getBounds();

		tagBtn = new Button(ctrlBar, SWT.PUSH);
		tagBtn.setImage(SWTWebKitBrowser.tagImg);
		tagBtn.setSize(bounds.width, bounds.height);
		tagBtn.setToolTipText("Add tag (to content of clipboard)");
		gd = new GridData(bounds.width, bounds.height);
		gd.horizontalIndent = 5;
		tagBtn.setLayoutData(gd);

		bounds = SWTWebKitBrowser.tagImg.getBounds();


		starBtn = new Button(ctrlBar, SWT.PUSH);
		starBtn.setImage(SWTWebKitBrowser.starImg);
		starBtn.setSize(bounds.width, bounds.height);
		starBtn.setToolTipText("Facebook Feed");
		gd = new GridData(bounds.width, bounds.height);
		gd.horizontalIndent = 5;
		starBtn.setLayoutData(gd);

		bounds = SWTWebKitBrowser.homeImg.getBounds();

		homeBtn = new Button(ctrlBar, SWT.PUSH);
		homeBtn.setImage(SWTWebKitBrowser.homeImg);
		homeBtn.setToolTipText("Facebook Messages");
		gd = new GridData(bounds.width, bounds.height);
		gd.horizontalIndent = 5;
		homeBtn.setLayoutData(gd);

		bounds = SWTWebKitBrowser.menuImg.getBounds();

		menuBtn = new Button(ctrlBar, SWT.PUSH);
		menuBtn.setImage(SWTWebKitBrowser.menuImg);
		menuBtn.setToolTipText("Facebook Notifications");
		gd = new GridData(bounds.width, bounds.height);
		gd.horizontalIndent = 5;
		menuBtn.setLayoutData(gd);

		Label spacer = new Label(ctrlBar, SWT.NONE);
		spacer.setText(" ");
		gd = new GridData();
		gd.horizontalIndent = 10;
		spacer.setLayoutData(gd);

		Label underline = new Label(ctrlBar, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BOTTOM;
		gd.horizontalSpan = 9;
		underline.setLayoutData(gd);

		// ---------------
		Composite browserComposite = new Composite(pageComposite, SWT.NONE);

		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		browserComposite.setLayout(layout);

		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		browserComposite.setLayoutData(gd);

		final WebKitBrowser browser = new WebKitBrowser(browserComposite,
				SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		browser.setLayoutData(gd);

		tabItem.setControl(pageComposite);
		BrowserHandler handler = new BrowserHandler(browser,this);

		org.eclipse.swt.graphics.Point p=folder.getShell().getSize();
		/*if(p.x==768&&p.y==530){x=630;y=85;}//1024
		else if(p.x==960&&p.y==722){x=890;y=85;}//1280
		else if(p.x==1080&&p.y==629){x=1050;y=85;}//1440
		else if(p.x==1200&&p.y==854){x=1210;y=85;}//1600*/
		if(p.x==768){x=620;y=85;}//1024
		else if(p.x==960){x=880;y=85;}//1280
		else if(p.x==1080){x=1040;y=85;}//1440
		else if(p.x==1200){x=1200;y=85;}//1600
		System.out.println(p.x+" "+p.y);
		final Button backBtnFinal = backBtn;
		backBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.back();
			}
		});

		final Button fwdBtnFinal = fwdBtn;
		fwdBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.forward();
			}
		});

		final Button reloadBtnFinal=reloadBtn;
		reloadBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.refresh();
			}
		});

		tagBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWTWebKitBrowser.loggedin){
					String tag="";
					Transferable t=java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
					try {
						if(t!=null&&t.isDataFlavorSupported(DataFlavor.stringFlavor))
							tag=(String)t.getTransferData(DataFlavor.stringFlavor);
					}catch(Exception ex){ex.printStackTrace();}
					String url=SWTWebKitBrowser.TAG_URL+"?tag="+URLEncoder.encode(tag)+"&accessToken="+URLEncoder.encode(SWTWebKitBrowser.accessToken);
					try{
					URL u=new URL(url);
					HttpURLConnection uc;
					uc=(HttpURLConnection)u.openConnection();
					uc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.29 (KHTML, like Gecko) Chrome/12.0.733.0 Safari/534.29");
					uc.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					uc.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
					uc.setRequestMethod("GET");
					uc.setInstanceFollowRedirects(false);
					BufferedReader br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
					String temp="",temp1="";
					while((temp=br.readLine())!=null){temp1+=temp;}
					if(temp1.equals("done")){
						ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Tag added");
						about.setMessage("Your tag is successfully added");
						about.open();
					}
					br.close();
					}catch(Exception ex){ex.printStackTrace();
						ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Some error occurred");
						about.setMessage("Some error has occurred");
						about.open();
					}
				}
				else{
					ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Not Logged in");
					about.setMessage("You are not logged in to browser.Type @home to go to login and the homepage of browser");
					about.open();
				}
			}
		});

		starBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWTWebKitBrowser.loggedin){
				Frames f=new Frames(x,y,"feed");
				while(Frames.url.equals("")){}
				if(Frames.url.equals("done")){Frames.url="";}
				else {browser.setUrl(awesm(Frames.url));addressTxt.setText(awesm(Frames.url));}
				}
				else{
					ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Not Logged in");
					about.setMessage("You are not logged in to browser.Type @home to go to login and the homepage of browser");
					about.open();
				}
			}
		});

		final Text addressTxtFinal = addressTxt;
		addressTxt.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String url=addressTxtFinal.getText().trim();
				if(url.startsWith("@fbus")){
					String t=awesm(url);
					addressTxtFinal.setText(prevurl);
					ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Status update");
					about.setMessage(t);
					about.open();
				}
				else if(url.startsWith("@rec")){
					if(SWTWebKitBrowser.loggedin){
						url=SWTWebKitBrowser.RECOMMENDATION_URL+"?accessToken="+SWTWebKitBrowser.accessToken;
						System.out.println(url);
						browser.setUrl(awesm(url));
						addressTxtFinal.setText(awesm(url));
						prevurl=awesm(url);
					}
					else{
						browser.setUrl(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						prevurl=awesm(SWTWebKitBrowser.DEFAULT_HOME_URL);
					}
				}
				else if(url.startsWith("@help")){
						browser.setUrl(awesm(SWTWebKitBrowser.HELP_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.HELP_URL));
						prevurl=awesm(SWTWebKitBrowser.HELP_URL);
				}
				else if(url.startsWith("@home")){
					if(SWTWebKitBrowser.loggedin){
						browser.setUrl(awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL));
						prevurl=awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL);
					}
					else{
						browser.setUrl(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						prevurl=awesm(SWTWebKitBrowser.DEFAULT_HOME_URL);
					}
				}
				else{if(!url.isEmpty()) {
					browser.setUrl(awesm(url));
					addressTxtFinal.setText(awesm(url));
					prevurl=awesm(url);
				}}
			}
		});

		final Button goBtnFinal = goBtn;
		goBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String url = addressTxtFinal.getText().trim();
				if(url.startsWith("@fbus")){
					if(SWTWebKitBrowser.loggedin){
						String t=awesm(url);
						addressTxtFinal.setText(prevurl);
						ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Status update");
						about.setMessage(t);
						about.open();
					}
					else{
						ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Not Logged in");
						about.setMessage("You are not logged in to browser.Type @home to go to login and the homepage of browser");
						about.open();
					}
				}
				else if(url.startsWith("@home")){
					if(SWTWebKitBrowser.loggedin){
						browser.setUrl(awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL));
						prevurl=awesm(SWTWebKitBrowser.LOGGEDIN_HOME_URL);
					}
					else{
						browser.setUrl(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.DEFAULT_HOME_URL));
						prevurl=awesm(SWTWebKitBrowser.DEFAULT_HOME_URL);
					}
				}

				else if(url.startsWith("@help")){
						browser.setUrl(awesm(SWTWebKitBrowser.HELP_URL));
						addressTxtFinal.setText(awesm(SWTWebKitBrowser.HELP_URL));
						prevurl=awesm(SWTWebKitBrowser.HELP_URL);
				}

				else{if (!url.isEmpty()) {
					browser.setUrl(awesm(url));
					addressTxtFinal.setText(awesm(url));
					prevurl=awesm(url);
				}}
			}
		});

		// final Button homeBtnFinal = homeBtn;
		homeBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWTWebKitBrowser.loggedin){
				Frames f=new Frames(x+starBtn.getSize().x+5,y,"inbox");
				while(Frames.url.equals("")){}
				if(Frames.url.equals("done")){Frames.url="";}
				else {browser.setUrl(awesm(Frames.url));addressTxt.setText(awesm(Frames.url));}
				}
				else{
					ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Not Logged in");
					about.setMessage("You are not logged in to browser.Type @home to go to login and the homepage of browser");
					about.open();
				}
			}
		});

		menuBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println(homeBtn.getLocation());
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWTWebKitBrowser.loggedin){
				Frames f=new Frames(x+starBtn.getSize().x+homeBtn.getSize().x+10,y,"notifications");
				while(Frames.url.equals("")){}
				if(Frames.url.equals("done")){Frames.url="";}
				else {browser.setUrl(awesm(Frames.url));addressTxt.setText(awesm(Frames.url));}
				}
				else{
					ReuseableDialog about = new ReuseableDialog(tabFolder.getShell(), "Not Logged in");
					about.setMessage("You are not logged in to browser.Type @home to go to login and the homepage of browser");
					about.open();
				}
			}
		});

		addressTxt.setText(awesm(Util.getHomeURL()));
		browser.setUrl(awesm(Util.getHomeURL()));
		prevurl=awesm(Util.getHomeURL());
		return tabItem;
	}
	
	public Shell getShell() {
		return getTabFolder().getShell();
	}
	
	public CTabFolder getTabFolder() {
		return tabFolder;
	}
	
	public CTabItem getTabItem() {
		return tabItem;
	}
	
	class BrowserHandler extends WebKitBrowserAdapter {
		BrowserPage browserPage;
		
		BrowserHandler(WebKitBrowser browser, BrowserPage page) {
			super(browser);
			browserPage = page;
		}

		@Override
		public void changing(LocationEvent event) {
			System.out.println("changing: " + event);

			Util.runAsync(new Runnable() {
				@Override
				public void run() {
					goBtn.setImage(SWTWebKitBrowser.stopImg);
				}
			});
		}

		@Override
		public void changed(final LocationEvent event) {
			System.out.println("changed: " + event);
			if(event.location.equals(SWTWebKitBrowser.CHECK_URL)){
				String txt=getBrowser().getText();
				System.out.println(txt);
				SWTWebKitBrowser.accessToken=txt.substring(txt.indexOf("accesstoken=")+12,txt.indexOf("&amp;endofaccesstoken"));
				System.out.println(SWTWebKitBrowser.accessToken);
				SWTWebKitBrowser.loggedin=true;
				getBrowser().setUrl(SWTWebKitBrowser.LOGGEDIN_HOME_URL);
				SWTWebKitBrowser.DEFAULT_HOME_URL=SWTWebKitBrowser.LOGGEDIN_HOME_URL;
				Util.updateAddressText(addressTxt,SWTWebKitBrowser.LOGGEDIN_HOME_URL);
			}
			Util.runAsync(new Runnable() {
				@Override
				public void run() {
					backBtn.setEnabled(getBrowser().isBackEnabled());
					fwdBtn.setEnabled(getBrowser().isForwardEnabled());
					goBtn.setImage(SWTWebKitBrowser.goImg);
					Util.updateAddressText(addressTxt,event.location);
				}
			});
		}

		@Override
		public void changed(ProgressEvent event) {
			// start
			System.out.println("changed: " + event);
		}

		@Override
		public void completed(ProgressEvent event) {
			// disable activityIndicator
			System.out.println("completed: " + event);
		}

		@Override
		public void changed(final TitleEvent event) {
			// update tabitem title & window title

			Util.runAsync(new Runnable() {
				@Override
				public void run() {
					Util.updateTabItemTitle(tabItem, event.title);
					Util.updateWindowTitle(browserPage.getShell(), event.title);
				}
			});
		}

		public void open(WindowEvent event) {
			System.out.println("open: " + event);
		}

		public void show(WindowEvent event) {
			System.out.println("show: " + event);
		}
	}


	class ReuseableDialog {

		private MessageBox box;

		public ReuseableDialog(Shell shell, String text) {
			box = new MessageBox(shell);
			box.setText(text);
		}

		public void setMessage(String message) {
			box.setMessage(message);
		}

		public int open() {
			return box.open();
		}
	}
}