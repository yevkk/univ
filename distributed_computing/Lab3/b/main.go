package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var waitingClients = 0
var barberSleeping = false
var waitingRoom = sync.NewCond(&sync.Mutex{})
var barberAvailable = sync.NewCond(&sync.Mutex{})
var haircutDone = sync.NewCond(&sync.Mutex{})

var barberCounter = 0;
func barber() {
	for {
		waitingRoom.L.Lock()
		if waitingClients == 0 {
			fmt.Println("\tBarber went sleep")
			barberSleeping = true
			waitingRoom.Wait()
			barberSleeping = false
		}
		barberCounter++
		waitingRoom.L.Unlock()
		barberAvailable.Signal()
		fmt.Printf("\tBarber took client #%v\n", barberCounter)
		time.Sleep(1 * time.Second)
		fmt.Printf("\tBarber finished his work\n")
		haircutDone.Signal()
	}
}

var clientCounter = 0
func client() {
	waitingRoom.L.Lock()
	clientCounter++
	id := clientCounter
	fmt.Printf("Client #%v came\n", id)
	if barberSleeping {
		waitingRoom.L.Unlock()
		waitingRoom.Signal()
	} else {
		waitingClients++
		waitingRoom.L.Unlock()

		barberAvailable.L.Lock()
		barberAvailable.Wait()
		barberAvailable.L.Unlock()
	}
	haircutDone.L.Lock()
	haircutDone.Wait()
	haircutDone.L.Unlock()
	fmt.Printf("Client #%v left\n", id)
}

func main() {
	go barber()
	for {
		time.Sleep(time.Duration(500 + rand.Intn(1000)) * time.Millisecond)
		go client()
	}
}

