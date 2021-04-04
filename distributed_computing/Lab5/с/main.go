package main

import (
	"fmt"
	"math/rand"
	"time"
)

func sum(arr []int) int {
	res := 0
	for _, v := range arr {
		res += v
	}
	return res
}


var r = rand.New(rand.NewSource(time.Now().UnixNano()))
func arrayHandler(id int, length int, inChan chan int, outChan chan int, done *bool) {
	arr := make([]int , length)
	for i := range arr {
		arr[i] = r.Intn(10)
	}

	for !(*done) {
		localSum := sum(arr)
		fmt.Printf("%v: %v\n", id, arr)
		outChan <- localSum

		globalAverage := <- inChan
		index := r.Intn(len(arr))
		switch {
		case globalAverage < localSum:
			arr[index]--
		case globalAverage > localSum:
			arr[index]++
		}
	}
}

func main() {
	done := false
	sendingChan := make(chan int, 3)
	receivingChan := make(chan int, 3)

	go arrayHandler(1, 10, sendingChan, receivingChan, &done)
	go arrayHandler(2, 10, sendingChan, receivingChan, &done)
	go arrayHandler(3, 10, sendingChan, receivingChan, &done)

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
			done = true
			return
		}
	}
}


