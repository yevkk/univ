package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func randNodes(graphSize int) []int {
	i, j := 1, 1
	for i == j {
		i, j = rand.Intn(graphSize), rand.Intn(graphSize)
	}
	return []int{i, j}
}

func edgeModifier(graph *Graph, lock *sync.RWMutex) {
	for {
		time.Sleep(time.Duration(500 + rand.Intn(1000)) * time.Millisecond)
		lock.Lock()
		fmt.Println("edgeModifier locked")
		if graph.size < 2 {
			fmt.Println("edgeModifier unlocked")
			lock.Unlock()
			continue
		}
		nodes := randNodes(graph.size)
		i, j := nodes[0], nodes[1]
		if rand.Intn(10) > 4 {
			graph.updateEdge(i, j, rand.Intn(9) + 1)
		} else {
			graph.updateEdge(i, j, 0)
		}
		fmt.Println("edgeModifier unlocked")
		lock.Unlock()
	}
}

func nodeModifier(graph *Graph, lock *sync.RWMutex) {
	for {
		time.Sleep(time.Duration(500 + rand.Intn(1000)) * time.Millisecond)
		lock.Lock()
		fmt.Println("nodeModifier locked")
		if rand.Intn(10) > 4 {
			graph.addNode()
		} else {
			if graph.size < 1 {
				fmt.Println("nodeModifier unlocked")
				lock.Unlock()
				continue
			}
			graph.delNode(rand.Intn(graph.size))
		}
		fmt.Println("nodeModifier unlocked")
		lock.Unlock()
	}
}

func pathFinder(graph *Graph, lock *sync.RWMutex) {
	for {
		time.Sleep(time.Duration(500 + rand.Intn(1000)) * time.Millisecond)
		lock.RLock()
		fmt.Println("pathFinder locked")
		if graph.size < 2 {
			fmt.Println("pathFinder unlocked")
			lock.RUnlock()
			continue
		}
		nodes := randNodes(graph.size)
		i, j := nodes[0], nodes[1]
		time.Sleep(500 * time.Millisecond)
		graph.findPaths(i, j)
		fmt.Println("pathFinder unlocked")
		lock.RUnlock()
	}
}

func main() {
	data := make([][]int, 0)
	g := &Graph{data, 0}
	lock := &sync.RWMutex{}

	go nodeModifier(g, lock)
	go edgeModifier(g, lock)
	go pathFinder(g, lock)
	go pathFinder(g, lock)

	for {}
}
