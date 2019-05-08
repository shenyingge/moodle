package com.example.ks.moodle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ks.moodle.util.TreeViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SuperTreeViewAdapter extends BaseExpandableListAdapter{
    static public class SuperTreeNode{
        Object parent;
        List<TreeViewAdapter.TreeNode> childs=new ArrayList<TreeViewAdapter.TreeNode>();
    }
    private List<SuperTreeNode>superTreeNodes=new ArrayList<SuperTreeNode>();
    private Context parentContext;
    private ExpandableListView.OnChildClickListener stvClickEvent;//外部回调函数
    public SuperTreeViewAdapter(Context view, ExpandableListView.OnChildClickListener stvClickEvent){
        parentContext=view;
        this.stvClickEvent=stvClickEvent;
    }
    public List<SuperTreeNode> getTreeNode(){
        return superTreeNodes;
    }
    public void UpdateTreeNode(List<SuperTreeNode> node){
        superTreeNodes=node;

    }
    public void removeAll(){
        superTreeNodes.clear();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return superTreeNodes.get(groupPosition).childs.get(childPosition);
    }
    public int getChildrenCount(int groupPosition) {
        return superTreeNodes.get(groupPosition).childs.size();
    }
    public ExpandableListView getExpandableListView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, TreeViewAdapter.ItemHeight);
        ExpandableListView superTreeView = new ExpandableListView(parentContext);
        superTreeView.setLayoutParams(lp);
        return superTreeView;
    }
    /**
     * 三层树结构中的第二层是一个ExpandableListView
     */
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // 是
        final ExpandableListView treeView = getExpandableListView();
        final TreeViewAdapter treeViewAdapter = new TreeViewAdapter(this.parentContext,0);
        List<TreeViewAdapter.TreeNode> tmp = treeViewAdapter.getTreeNodes();//临时变量取得TreeViewAdapter的TreeNode集合，可为空
        final TreeViewAdapter.TreeNode treeNode=(TreeViewAdapter.TreeNode) getChild(groupPosition, childPosition);
        tmp.add(treeNode);
        treeViewAdapter.UpdateTreeNode(tmp);
        treeView.setAdapter(treeViewAdapter);
        //关键点：取得选中的二级树形菜单的父子节点,结果返回给外部回调函数
        treeView.setOnChildClickListener(this.stvClickEvent);
        /**
         * 关键点：第二级菜单展开时通过取得节点数来设置第三级菜单的大小
         */
        treeView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        (treeNode.childs.size()+1)*TreeViewAdapter.ItemHeight + 10);
                treeView.setLayoutParams(lp);
            }
        });
        /**
         * 第二级菜单回收时设置为标准Item大小
         */
        treeView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        TreeViewAdapter.ItemHeight);
                treeView.setLayoutParams(lp);
            }
        });
        treeView.setPadding(TreeViewAdapter.PaddingLeft, 0, 0, 0);
        return treeView;
    }
    /**
     * 三级树结构中的首层是TextView,用于作为title
     */
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TextView textView = TreeViewAdapter.getTextView(this.parentContext);
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(TreeViewAdapter.PaddingLeft, 0, 0, 0);
        return textView;
    }
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    public Object getGroup(int groupPosition) {
        return superTreeNodes.get(groupPosition).parent;
    }
    public int getGroupCount() {
        return superTreeNodes.size();
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public boolean hasStableIds() {
        return true;
    }
}
