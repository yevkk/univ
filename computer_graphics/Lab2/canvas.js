'use strict'

const POINT_RADIUS = 5

// 0 - drawing graph; 1 - setting point; 2 - animating result; 3 - needs restart
let stage = 0
let proceedBtnText = ['Preprocessing', 'Run']
let selectedPoint = null
let mainCanvas


function drawPoint(context, color, point) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, POINT_RADIUS, 0, 2 * Math.PI)
    context.fill()
}

function drawEdge(context, color, edge) {
    context.strokeStyle = color
    context.beginPath()
    context.moveTo(edge.start.x, edge.start.y)
    context.lineTo(edge.end.x, edge.end.y)
    context.stroke()
}

function drawGraph(canvas, graph) {
    let context = canvas.getContext('2d')
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
    for (let point of graph.nodes) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--main-color'), point)
    }
    for (let edge of graph.edges) {
        drawEdge(context, window.getComputedStyle(canvas).getPropertyValue('--main-color'), edge)
    }
    if (selectedPoint) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--highlight-color'), selectedPoint)
    }
}

function onCanvasClick(e) {
    let context = mainCanvas.getContext('2d');
    switch (stage) {
        case 0:
            let x = e.pageX - mainCanvas.offsetLeft
            let y = e.pageY - mainCanvas.offsetTop
            let point = graph.nodes.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)
            if (point) {
                if (selectedPoint) {
                    if (selectedPoint === point) {
                        showMessage(`you should select another point`, `warning`)
                    } else if (!graph.edges.find(item => (item.start === point && item.end === selectedPoint) || (item.start === selectedPoint && item.end === point))) {
                        let edge = new Edge(selectedPoint, point)
                        graph.edges.push(edge)
                        showMessage(`added edge (${edge.start.x}, ${edge.start.y}) - (${edge.end.x}, ${edge.end.y})`, `log`)
                        selectedPoint = null
                    } else {
                        showMessage(`edge already exists`, `info`)
                        selectedPoint = null
                    }
                } else {
                    if (graph.nodes.length > 1) {
                        selectedPoint = point
                        showMessage(`selected point (${point.x}, ${point.y})`, `log`)
                    } else {
                        showMessage(`you should create at least 2 points`, `warning`)
                    }
                }
            } else {
                if (selectedPoint) {
                    showMessage(`you should select a point`, `warning`)
                } else {
                    point = new Node(x, y)
                    graph.nodes.push(point)
                    showMessage(`added point (${point.x}, ${point.y})`, `log`)
                }
            }
            if (graph.nodes.length > 2 && graph.edges.length > 1) {
                let proceedBtn = document.getElementById('proceed-button')
                if (!proceedBtn.classList.contains('active-button')) {
                    proceedBtn.classList.add('active-button')
                }
            }
            drawGraph(mainCanvas, graph)
            break
        default:
            break
    }
}

function proceed() {
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        return
    }
    switch (stage) {
        case 0:
            if (graph.containsCrossingEdges()) {
                stage = 3;
                proceedBtn.classList.remove('active-button')
                showMessage(`graph contains crossing edges; reset required`, `warning`)
            }
            graph.nodes.sort((point1, point2) => (point1.y !== point2.y) ? (point2.y - point1.y) : (point1.x - point2.x))
            //graph.makeRegular()
            //graph.weightBalance()
            //graph.buildChains()
            stage++
            proceedBtn.innerText = proceedBtnText[stage]
            break
        default:
            break
    }

}

function reset() {
    graph.clear()
    stage = 0
    selectedPoint = null
    drawGraph(mainCanvas, graph)
    let proceedBtn = document.getElementById('proceed-button')
    proceedBtn.innerText = proceedBtnText[stage]
    if (proceedBtn.classList.contains('active-button')) {
        proceedBtn.classList.remove('active-button')
    }
    showMessage('Add at least 3 point and 2 edges', 'tip')
}

function showMessage(msg, className) {
    let console = document.getElementById('console')
    let newMessage = document.createElement('p')
    newMessage.classList.add(className)
    newMessage.innerText = `[${className}] ${msg}`
    console.appendChild(newMessage)
    console.scroll(0, console.scrollHeight)
}

addEventListener('load', () => {
    mainCanvas = document.getElementById('main-canvas')
    mainCanvas.width = mainCanvas.parentElement.clientWidth * 0.7
    mainCanvas.height = 500
    mainCanvas.addEventListener('click', onCanvasClick)

    document.getElementById('proceed-button').addEventListener('click', proceed)
    document.getElementById('reset-button').addEventListener('click', () => {
        showMessage('Canvas cleared', 'info')
        reset()
    })
    reset()
})

