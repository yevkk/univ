package main

import (
	"fmt"
	"strings"
)

type CompetitionTreeNode struct {
	left  *CompetitionTreeNode
	right *CompetitionTreeNode
	key   *Monk
}

// considered level was calculated big enough
func (node *CompetitionTreeNode) printNode(level int) {
	if node.left != nil {
		node.left.printNode(level - 1)
	}
	fmt.Printf("%v%v\n", strings.Repeat("\t\t\t|", level), (node.key).String())
	if node.right != nil {
		node.right.printNode(level - 1)
	}
}