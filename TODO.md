DONE:<s>
- [FEATURE] add option to open article in webview or choose external app
- [FEATURE] fix settings checkbox ugliness
- [FEATURE] add source discovery from url
- [FEATURE] add intro setup
- [FEATURE] add select settings (metered network, refresh interval)
page in walkthrough
- [FEATURE] make "last refresh" time in source list refresh every 1 sec
- [FEATURE] add theme selection (as setting and in walkthrough)
- [FEATURE] add a device-info list screen
- [FEATURE] add networks and audio manager device info
- [BUG] add-source screen, try to save a source with invalid name and/or url, error, then try to save a valid one
- [BUG] first add many sources, in source list scroll to bottom, the last source is covered by "add" button
- [BUG] swipe delete a source from list, the snackbar doesnt push the "+" button
- [BUG] sources in source list have identicons even if favicon is available
- [BUG] it seems that splitting base settings broke them, app theme changes are not being notified
- [BUG] !! sometimes in discover url (from articles screen) some leftover from walkthrough discovery are present, actually duplicates appear also in walkthrough
- [BUG] there is no way to stop a discover url, not even a timeout
- [BUG] !! post 2 view action events from a view model one after the other, only the second one is received (SingleLiveData)
- [BUG] in sources list screen, if there is no source the screen is empty
- [DEBT] find way to mock both view model and http layers in ui tests
- [DEBT] add ui smoke tests
- [DEBT] create instrumentation tests for AppSettingsImpl
- [DEBT] create delegate properties or something for AppSettingsImpl
- [DEBT] add steps to smoke test to verify use metered network, articles images
- [DEBT] add steps to smoke test to verify all recycler view adapters (for refactoring)
- [DEBT] unify RecyclerView adapter with base class (FoundFeedsAdapter, ArticlesAdapter)
- [DEBT] add smoke test: 2 sources active, count articles, disable 1 source, count...
- [DEBT] remove robolectric now seriously
- [DEBT] go through unit-test and replace non-needed mock instances to speed up the execution
- [DEBT] use only deep links for navigation
- [DEBT] move logic from BaseActivity to a BaseActivityViewModel
- [DEBT] check if subscribing on new thread when doing http, should use a thread pool
</s>

DEBT:
- [DEBT] ui-unit tests for all activities
- [DEBT] make in-app article web view use OkHttp and adapt smoke test accordingly 
- [DEBT] FeedFinderIntegrationTest.findsLinksInHtml2 is blocking >_<

FEATURE:
- [FEATURE] add option to choose which external app is used to open articles
- [FEATURE] add refresher background job
- [FEATURE] add single-source-screen

BUGS:
- [BUG] found feeds list includes http - https cloned urls, should probably remove http ones
- [BUG] pop up theme for overflow menu in articles list is always light even on darcula theme
- [BUG] smoke tests are flaky
- [BUG] the launcher activity is visible 
- [BUG] in the walkthrough the highlight color for text is wrong, the text is white but if highlighted it turns black :/ 

IN PROGRESS:
- [FEATURE] add intro page in walkthrough (why type in url in discovery)
