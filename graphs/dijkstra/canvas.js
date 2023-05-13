'use strict'

const POINT_RADIUS = 5

// 0 - drawing graph; 1 - setting start node; 2 - animating result; 3 - needs restart
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
    context.lineWidth = 2
    context.beginPath()
    context.moveTo(edge.start.x, edge.start.y)
    context.lineTo(edge.end.x, edge.end.y)
    context.stroke()
}

function drawGrid(canvas) {
    let context = canvas.getContext('2d')
    context.strokeStyle = window.getComputedStyle(canvas).getPropertyValue('--grid-color')
    context.lineWidth = 0.5

    for (let i = 10; i < canvas.width; i += 10) {
        context.beginPath()
        context.moveTo(i, 10)
        context.lineTo(i, canvas.height - 10)
        context.stroke()
    }

    for (let i = 10; i < canvas.height; i += 10) {
        context.beginPath()
        context.moveTo(10, i)
        context.lineTo(canvas.width - 10, i)
        context.stroke()
    }
}

function drawGraph(canvas, graph, specialPoint) {
    let context = canvas.getContext('2d')
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
    drawGrid(canvas)
    for (let edge of graph.edges) {
        drawEdge(context, window.getComputedStyle(canvas).getPropertyValue('--edge-color'), edge)
    }
    for (let point of graph.points) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--point-color'), point)
    }
    if (selectedPoint) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--highlight-color'), selectedPoint)
    }
    if (specialPoint) {
        drawPoint(context, window.getComputedStyle(canvas).getPropertyValue('--special-point-color'), specialPoint)
    }
}

function onCanvasClick(e) {
    let context = mainCanvas.getContext('2d');
    let x = e.pageX - mainCanvas.offsetLeft
    let y = e.pageY - mainCanvas.offsetTop
    let point = graph.points.find(item => Math.abs(x - item.x) < POINT_RADIUS && Math.abs(y - item.y) < POINT_RADIUS)
    switch (stage) {
        case 0:
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
                    if (graph.points.length > 1) {
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
                    point = new Point(x, y)
                    graph.points.push(point)
                    showMessage(`added point (${point.x}, ${point.y})`, `log`)
                }

            }
            if (graph.points.length > 1) {
                let proceedBtn = document.getElementById('proceed-button')
                if (!proceedBtn.classList.contains('active-button')) {
                    proceedBtn.classList.add('active-button')
                }
            }
            drawGraph(mainCanvas, graph)
            break
        case 1:
            // TODO: implement point selection
        default:
            break
    }
}

function proceed() {
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        return
    }
    proceedBtn.classList.remove('active-button')
    switch (stage) {
        case 0:
            if (graph.containsCrossingEdges()) {
                stage = 3;
                showMessage(`graph contains crossing edges; reset required`, `warning`)
                return
            }
            stage++
            proceedBtn.innerText = proceedBtnText[stage]

            showMessage(`select point to localize and run`, `tip`)
            break
        case 1:
            stage++

            // run algorithm

            showMessage(`reset to restart`, `tip`)
            break
        default:
            break
    }

}

function reset() {
    graph.clear()
    stage = 0
    selectedPoint = undefined
    drawGraph(mainCanvas, graph)
    let proceedBtn = document.getElementById('proceed-button')
    proceedBtn.innerText = proceedBtnText[stage]
    if (proceedBtn.classList.contains('active-button')) {
        proceedBtn.classList.remove('active-button')
    }
    showMessage('Add at least 2 points', 'tip')
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

