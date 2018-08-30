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
</s>

DEBT:
- find way to mock both view model and http layers in ui tests
- ui-unit tests for all activities
- add ui smoke tests
- unify RecyclerView adapter with base class (FoundFeedsAdapter, ArticlesAdapter)

FEATURE:
- add option to choose which external app is used to open articles
- add refresher background job
- add single-source-screen
- add intro page in walkthrough (why type in url in discovery)
- add theme selection (as setting and in walkthrough)

BUGS:
- found feeds list includes http - https cloned urls, should probably remove http ones
- !! post 2 view action events from a view model one after the other, only the second one is received (SingleLiveData)

IN PROGRESS:
- [FEATURE] make "last refresh" time in source list refresh every 1 min

