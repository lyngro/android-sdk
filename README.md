# LynGro Android SDK

## Initialize Lyngro Tracker ##
    private LyngroTracker mLyngroTracker;

    public synchronized LyngroTracker getTracker() {
        if (mLyngroTracker == null) {
            try {
                String LyngroAppKey = getResources().getString(R.string.Lyngro_app_key);
                String LyngroAppDomain = getResources().getString(R.string.Lyngro_app_domain);
                String LyngroWidget = getResources().getString(R.string.Lyngro_widgetid);
                Lyngro LyngroInstance = Lyngro.getInstance(this);
                mLyngroTracker = LyngroInstance.newTracker( LyngroAppKey, LyngroAppDomain,LyngroWidget);
                mLyngroTracker.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                mLyngroTracker.setDispatchInterval(15);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("LyngroTracker URL was malformed.");
            }
        }
        return mLyngroTracker;
    }
    
    
    
## Send Page views ##
        tracker = ((SharedClass) getApplication()).getTracker();

        final String targetingextra = this.getIntent().getStringExtra("targeting");
        LyngroHeartbeatHandler handler = tracker.sendPageView("18", "http://example.com/article?id=1");
        
## Send Time Spent

    @Override
    protected void onResume() {
        super.onResume();
        handler.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.pause();
    }
    
## Generate Recommendations
        recommendations = new RecommendationLayout(this, tracker, getResources().getInteger(R.integer.Lyngro_widget_items_count));
        ((LinearLayout) findViewById(R.id.action_container)).addView(recommendations);

#### Note Check RecommendationLayout in the demo folder ####
