package com.limn.frame.control.interfaceframe;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import com.limn.frame.panel.CustomPanel;

public class InterfaceTestPanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 接口树结构集合
	private JTree interfaceTree = null;

	private String parametersPath = System.getProperty("user.dir") + "/InterfaceData";

	private InterfaceTestEditPanel interfaceTestEdit = new InterfaceTestEditPanel();

	private DefaultMutableTreeNode interfaceTreeModel = null;

	private JButton edit = new JButton("编辑");
	private JButton cancel = new JButton("取消");
	
	public InterfaceTestPanel() {

		this.setLayout(null);

		interfaceTreeModel = getFiles(parametersPath, "接口");
		interfaceTree = new JTree(interfaceTreeModel);
		addComponent(interfaceTree, 10, 40, 280, 500);
		addComponent(interfaceTestEdit, 300, 0, 700, 600);

		interfaceTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) interfaceTree.getLastSelectedPathComponent();// 返回最后选定的节点
				String sel = selectedNode.toString();
				if(selectedNode.isLeaf() && !selectedNode.isRoot()){
					interfaceTestEdit.load(sel);
				}
			}
		});

		JButton add = new JButton("新建");
		addComponent(add, 10, 10, 60, 20);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interfaceTestEdit.createInterface();
			}
		});

		JButton save = new JButton("保存");
		addComponent(save, 80, 10, 60, 20);
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 树结构
				String interfaceName = interfaceTestEdit.save("");
				if (null != interfaceName) {
					interfaceTreeModel.add(new DefaultMutableTreeNode(interfaceName));
					interfaceTree.updateUI();
				}

			}
		});

		JButton delete = new JButton("删除");
		addComponent(delete, 150, 10, 60, 20);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) interfaceTree.getLastSelectedPathComponent();
				if (selectedNode != null && selectedNode.getParent() != null) {
					// 删除指定节点
					interfaceTreeModel.remove(selectedNode);
					interfaceTree.updateUI();
					interfaceTestEdit.delete(parametersPath + "/" + selectedNode.toString());
				}
			}
		});

		

		
		addComponent(edit, 220, 10, 60, 20);
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				interfaceTestEdit.setEditable(true);	
				cancel.setVisible(true);
				edit.setVisible(false);
			}
		});
		
		cancel.setVisible(false);
		addComponent(cancel, 220, 10, 60, 20);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) interfaceTree.getLastSelectedPathComponent();// 返回最后选定的节点
				String sel = selectedNode.toString();
				interfaceTestEdit.load(sel);
				cancel.setVisible(false);
				edit.setVisible(true);
				interfaceTestEdit.setEditable(false);
			}
		});
		
		
		// JButton request = new JButton("请求");
		// addComponent(request,150,10,60,20);
		// add.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// interfaceTestEdit.request();
		//
		// }
		// });
	}

	private void addComponent(Component comp, int x, int y, int width, int height) {
		this.add(comp);
		comp.setBounds(x, y, width, height);
	}

	private DefaultMutableTreeNode getFiles(String filePath, String key) {
		DefaultMutableTreeNode keyWordNode = new DefaultMutableTreeNode(key);
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {

			String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
			if (file.isDirectory()) {
				keyWordNode.add(getFiles(file.getAbsolutePath(), fileName));
			}
			keyWordNode.add(new DefaultMutableTreeNode(fileName));
		}
		return keyWordNode;
	}

}
