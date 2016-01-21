# MultiLevelListView

MultiLevelListView is an Open Source Android library providing view for displaying items
grouped into levels and sub-levels. Levels hierarchy size is not limited. Any particular data
model is not required, it fully depends on developer.

MultiLevelListView is simple in use. Like Android's ListView, it requires to extend and
provide an Adapter object (MultiLevelListAdapter). The adapter has three methods that have to be
implemented:

- boolean isExpandable(Object object)
  Indicates if particular data object is an expandable object

- List<?> getSubObjects(Object object)
  Get sub-objects for particular data object if the object is expandable

- View getViewForObject(Object object, View convertView, ItemInfo itemInfo)
  Get a view for displaying data object. Old view is provided to reuse if is available.
  Additionally ItemInfo object is provided with additional information about object and its
  location on the list. This information can be used for styling the view.

For detailed information how to use the library please check included sample application and source codes.

### Project integration

Add repository reference in your build.gradle file:


repositories {
    ...
    maven {
        url 'http://dev.open-rnd.net:30844/content/groups/public/'
    }
    ...
}


Add library dependence:


dependencies {
    compile group: "pl.openrnd.android", name: "multi-level-listview", version: "1.0.1"
}


### License

2016 (C) Copyright Open-RnD Sp. z o.o.

Licensed under the Apache License, Version 2.0 (the "License");<br />
you may not use this file except in compliance with the License.<br />
You may obtain a copy of the License at<br />

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software<br />
distributed under the License is distributed on an "AS IS" BASIS,<br />
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br />
See the License for the specific language governing permissions and<br />
limitations under the License.
