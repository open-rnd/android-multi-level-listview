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

package pl.openrnd.multilevellistview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.NestType;
import pl.openrnd.multilevellistview.OnItemClickListener;
import pl.openrnd.multilevellistview.sample.data.BaseItem;
import pl.openrnd.multilevellistview.sample.data.DataProvider;
import pl.openrnd.multilevellistview.sample.views.LevelBeamView;

public class DataActivity extends Activity {

    private MultiLevelListView mListView;
    private Switch mMultipliedExpandingView;
    private Switch mAlwaysExpandedView;
    private Switch mInitiallyExpandedView;

    private boolean mAlwaysExpandend;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confViews();
    }

    private void confViews() {
        setContentView(R.layout.data_activity);

        mListView = (MultiLevelListView) findViewById(R.id.listView);
        mMultipliedExpandingView = (Switch) findViewById(R.id.multipledExpanding);
        mAlwaysExpandedView = (Switch) findViewById(R.id.alwaysExpanded);
        mInitiallyExpandedView = (Switch) findViewById(R.id.initiallyExpanded);

        mMultipliedExpandingView.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mAlwaysExpandedView.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mInitiallyExpandedView.setOnCheckedChangeListener(mOnCheckedChangeListener);

        setAlwaysExpanded(mAlwaysExpandedView.isChecked());
        setMultipleExpanding(mMultipliedExpandingView.isChecked());

        mListAdapter = new ListAdapter();

        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mListAdapter.setDataItems(DataProvider.getInitialItems());
    }

    private void setAlwaysExpanded(boolean alwaysExpanded) {
        mAlwaysExpandend = alwaysExpanded;
        mListView.setAlwaysExpanded(alwaysExpanded);
    }

    private void setMultipleExpanding(boolean multipleExpanding) {
        mListView.setNestType(multipleExpanding ? NestType.MULTIPLE : NestType.SINGLE);
    }

    private void setInitiallyExpanded(boolean expanded) {
        DataProvider.setGroup2InitiallyExpanded(expanded);
        mListAdapter.reloadData();
    }


    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.multipledExpanding:
                    setMultipleExpanding(isChecked);
                    break;

                case R.id.alwaysExpanded:
                    setAlwaysExpanded(isChecked);
                    break;
                case R.id.initiallyExpanded:
                    setInitiallyExpanded(isChecked);
                    break;
            }
        }
    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            StringBuilder builder = new StringBuilder("\"");
            builder.append(((BaseItem) object).getName());
            builder.append("\" clicked!\n");
            builder.append(getItemInfoDsc(itemInfo));

            Toast.makeText(DataActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            return DataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return DataProvider.isExpandable((BaseItem) object);
        }

        @Override
        protected boolean isInitiallyExpanded(Object object) {
            return DataProvider.isInitiallyExpanded((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(DataActivity.this).inflate(R.layout.data_item, null);
                viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            viewHolder.infoView.setText(getItemInfoDsc(itemInfo));

            if (itemInfo.isExpandable() && !mAlwaysExpandend) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.arrow_up : R.drawable.arrow_down);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }
    }

    private String getItemInfoDsc(ItemInfo itemInfo) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("level[%d], idx in level[%d/%d]",
                itemInfo.getLevel() + 1, /*Indexing starts from 0*/
                itemInfo.getIdxInLevel() + 1 /*Indexing starts from 0*/,
                itemInfo.getLevelSize()));

        if (itemInfo.isExpandable()) {
            builder.append(String.format(", expanded[%b]", itemInfo.isExpanded()));
        }
        return builder.toString();
    }
}