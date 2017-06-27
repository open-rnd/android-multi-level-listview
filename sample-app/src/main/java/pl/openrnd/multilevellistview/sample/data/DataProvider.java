/******************************************************************************
 *
 *  2016 (C) Copyright Open-RnD Sp. z o.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package pl.openrnd.multilevellistview.sample.data;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    /**
     * Do not confuse with MultiLevelListView levels.
     * Following variables refer only to data generation process.
     * For instance, if ITEMS_PER_LEVEL = 2 and MAX_LEVELS = 3,
     * list should look like this:
     * + 1
     * | + 1.1
     * | - - 1.1.1
     * | - - 1.1.2
     * | + 1.2
     * | - - 1.2.1
     * | - - 1.2.2
     * | - - 1.2.3
     * + 2
     * | + 2.1
     * | - - 2.1.1
     * | - - 2.1.2
     * | + 2.2
     * | - - 2.2.1
     * | - - 2.2.2
     */
    private static final int ITEMS_PER_LEVEL = 4;
    private static final int MAX_LEVELS = 6;
    private static boolean isGroup2InitiallyExpanded = false;

    public static List<BaseItem> getInitialItems() {
        return getSubItems(new GroupItem("root"));
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {
        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        List<BaseItem> result = new ArrayList<>(ITEMS_PER_LEVEL);
        int nextLevel = groupItem.getLevel() + 1;

        int groupNr = 0;
        int itemNr = 0;
        for (int i = 0; i < ITEMS_PER_LEVEL; ++i) {
            BaseItem item;
            if (i % 2 == 0 && nextLevel != MAX_LEVELS) {
                item = new GroupItem("Group " + Integer.toString(++groupNr));
                ((GroupItem) item).setLevel(nextLevel);
            } else {
                item = new Item("Item " + Integer.toString(++itemNr));
            }
            result.add(item);
        }
        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    public static boolean isInitiallyExpanded(BaseItem baseItem) {
        return isGroup2InitiallyExpanded && baseItem.getName().startsWith("Group 2");
    }

    public static void setGroup2InitiallyExpanded(boolean expanded) {
        isGroup2InitiallyExpanded = expanded;
    }
}
