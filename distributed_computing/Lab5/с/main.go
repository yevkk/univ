package main

import (
	"fmt"
	"math/rand"
)

func sum(arr []int) int {
	res := 0
	for _, v := range arr {
		res += v
	}
	return res
}

func arrayHandler(id int, length int, inChan chan int, outChan chan int) {
	arr := make([]int , length)
	for i := range arr {
		arr[i] = rand.Intn(10)
	}

	for {
		localSum := sum(arr)
		fmt.Printf("%v: %v\n", id, arr)
		outChan <- localSum

		globalAverage := <- inChan
		index := rand.Intn(len(arr))
		switch {
		case globalAverage < localSum:
			arr[index]--
		case globalAverage > localSum:
			arr[index]++
		}
	}
}

func main() {
	sendingChan := make(chan int, 3)

	receivingChan := make(chan int, 3)

	go arrayHandler(1, 10, sendingChan, receivingChan)
	go arrayHandler(2, 10, sendingChan, receivingChan)
	go arrayHandler(3, 10, sendingChan, receivingChan)

	for {
		s1 := <- receivingChan
		s2 := <- receivingChan
		s3 := <- receivingChan


		fmt.Printf("%v, %v, %v\n\n", s1, s2, s3)

		average := (s1 + s2 + s3) / 3
		sendingChan <- average
		sendingChan <- average
		sendingChan <- average
		if s1 == s2 && s1 == s3 {
			return
		}
	}
}


