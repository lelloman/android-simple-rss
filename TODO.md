DONE:<s>
- [FEATURE] add option to open article in webview or choose external app
- [FEATURE] fix settings checkbox ugliness
- [FEATURE] add source discovery from url
- [FEATURE] add intro setup
- [FEATURE] add select settings (metered network, refresh interval)
page in walkthrough
- [BUG] add-source screen, try to save a source with invalid name and/or url, error, then try to save a valid one
- [BUG] first add many sources, in source list scroll to bottom, the last source is covered by "add" button
- [BUG] swipe delete a source from list, the snackbar doesnt push the "+" button
- [BUG] sources in source list have identicons even if favicon is available
- [FEATURE] make "last refresh" time in source list refresh every 1 sec
- [FEATURE] add theme selection (as setting and in walkthrough)
- [DEBT] find way to mock both view model and http layers in ui tests
- [DEBT] add ui smoke tests
- [DEBT] create instrumentation tests for AppSettingsImpl
- [DEBT] create delegate properties or something for AppSettingsImpl
- [DEBT] add steps to smoke test to verify use metered network, articles images
- [DEBT] add steps to smoke test to verify all recycler view adapters (for refactoring)
- [DEBT] unify RecyclerView adapter with base class (FoundFeedsAdapter, ArticlesAdapter)
</s>

DEBT:
- [DEBT] remove robolectric now seriously
- [DEBT] ui-unit tests for all activities
- [DEBT] make in-app article web view use OkHttp and adapt smoke test accordingly 
- [DEBT] go through unit-test and replace non-needed mock instances to speed up the execution

FEATURE:
- [FEATURE] add option to choose which external app is used to open articles
- [FEATURE] add refresher background job
- [FEATURE] add single-source-screen

BUGS:
- [BUG] found feeds list includes http - https cloned urls, should probably remove http ones
- [BUG] !! post 2 view action events from a view model one after the other, only the second one is received (SingleLiveData)
- [BUG] in sources list screen, if there is no source the screen is empty
- [BUG] pop up theme for overflow menu in articles list is always light even on darcula theme
- [BUG] there is no way to stop a discover url, not even a timeout
- [BUG] sometimes in discover url (from articles screen) some leftover from walkthrough discovery are present
- [BUG] smoke tests are flaky

IN PROGRESS:
- [FEATURE] add intro page in walkthrough (why type in url in discovery)
