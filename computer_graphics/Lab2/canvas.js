'use strict'

const POINT_RADIUS = 10

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
    context.lineWidth = 2
    context.beginPath()
    context.moveTo(edge.start.x, edge.start.y)
    context.lineTo(edge.end.x, edge.end.y)
    context.stroke()
}

function drawGraph(canvas, graph, specialPoint) {
    let context = canvas.getContext('2d')
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
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

function chainsAnimation(canvas, graph, chains, step) {
    let delay = 500
    if (step === undefined) {
        step = 0
        drawGraph(canvas, graph)
        setTimeout(() => {
            chainsAnimation(canvas, graph, chains, step)
        }, delay)
        return
    }
    let chain = chains[step]
    let color = window.getComputedStyle(canvas).getPropertyValue('--highlight-color')
    let context = canvas.getContext('2d')

    drawGraph(canvas, graph)
    for (let edge of chain.edges) {
        drawEdge(context, color, edge)
        drawPoint(context, color, edge.start)
    }
    drawPoint(context, color, chain.edges[chain.edges.length - 1].end)

    if (step === chains.length - 1) {
        setTimeout(() => {
            drawGraph(canvas, graph)
            chainsAnimation.finished = true
        }, delay)
    } else {
        setTimeout(() => {
            step++
            chainsAnimation(canvas, graph, chains, step)
        }, delay)
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
            if (graph.points.length > 2) {
                let proceedBtn = document.getElementById('proceed-button')
                if (!proceedBtn.classList.contains('active-button')) {
                    proceedBtn.classList.add('active-button')
                }
            }
            drawGraph(mainCanvas, graph)
            break
        case 1:
            if (!pointToCheck && chainsAnimation.finished) {
                if (point) {
                    pointToCheck = point
                } else {
                    pointToCheck = new Point(x, y)
                }
            }
            document.getElementById('proceed-button').classList.add('active-button')
            drawGraph(mainCanvas, graph, pointToCheck)
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
            graph.points.sort((point1, point2) => (point1.y !== point2.y) ? (point2.y - point1.y) : (point1.x - point2.x))
            graph.makeRegular()
            drawGraph(mainCanvas, graph)
            if (graph.containsCrossingEdges()) {
                stage = 3;
                proceedBtn.classList.remove('active-button')
                showMessage(`graph contains crossing edges; reset required`, `warning`)
                return
            }
            graph.weightBalance()
            stage++
            proceedBtn.innerText = proceedBtnText[stage]
            proceedBtn.classList.remove('active-button')
            chains = graph.buildChains()
            showMessage(`chains built`, `info`)

            chainsAnimation.finished = false
            chainsAnimation(mainCanvas, graph, chains)

            showMessage(`select point to localize`, `tip`)
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
    showMessage('Add at least 3 points', 'tip')
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

