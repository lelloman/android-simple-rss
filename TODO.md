DONE:

- [FEATURE]<s>add option to open article in webview or choose external app</s>
- [FEATURE]<s>fix settings checkbox ugliness</s>
- [FEATURE]<s>add source discovery from url</s>

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
- make "last refresh" time in source list refresh every 1 min
- add theme selection (as setting and in walkthrough)

BUGS:
- add-source screen, try to save a source with invalid name and/or url, error, then try to save a valid one
- first add many sources, in source list scroll to bottom, the last source is covered by "add" button
- swipe delete a source from list, the snackbar doesnt push the "+" button
- found feeds list includes http - https cloned urls, should probably remove http ones
- sources in source list have identicons even if favicon is available
- !! post 2 view action events from a view model one after the other, only the second one is received (SingleLiveData)

IN PROGRESS:
- [FEATURE] add intro setup
- [FEATURE] add select settings (metered network, refresh interval)
page in walkthrough

