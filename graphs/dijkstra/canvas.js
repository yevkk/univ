'use strict'

const POINT_RADIUS = 15

// 0 - drawing graph; 1 - setting start node; 2 - needs restart
let stage = 0
let proceedBtnText = ['Preprocessing', 'Run']
let selectedPoint = null
let mainCanvas


function drawPoint(context, point, color, text, text_color) {
    context.fillStyle = color
    context.beginPath()
    context.arc(point.x, point.y, POINT_RADIUS, 0, 2 * Math.PI)
    context.fill()

    if (text != null) {
        context.fillStyle = text_color
        context.textAlign = 'center'
        context.font = '17px "Trebuchet MS", sans-serif'
        let metrics = context.measureText(text)
        let text_height = metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent
        context.fillText(text, point.x, point.y + text_height / 2);
    }
}

function drawEdge(context, edge, color, text, text_color) {
    context.strokeStyle = color
    context.lineWidth = 2
    context.beginPath()
    context.moveTo(edge.start.x, edge.start.y)
    context.lineTo(edge.end.x, edge.end.y)
    context.stroke()

    if (text != null) {
        context.strokeStyle = 'white'
        context.lineWidth = 4
        context.fillStyle = text_color
        context.textAlign = 'center'
        context.font = 'bold 17px "Trebuchet MS", sans-serif'

        let metrics = context.measureText(text)
        let text_height = metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent
        let x = (edge.start.x + edge.end.x) / 2
        let y = (edge.start.y + edge.end.y + text_height) / 2

        context.strokeText(text, x, y);
        context.fillText(text, x, y);
    }
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
        drawEdge(context,
                 edge,
                 window.getComputedStyle(canvas).getPropertyValue('--edge-color'),
                 edge.value,
                 window.getComputedStyle(canvas).getPropertyValue('--edge-color'))
    }
    for (let point of graph.points) {
        drawPoint(context, 
                  point,
                  window.getComputedStyle(canvas).getPropertyValue('--point-color'),
                  point.value,
                  window.getComputedStyle(canvas).getPropertyValue('--point-text-color'))
    }
    if (selectedPoint) {
        drawPoint(context, 
                  selectedPoint,
                  window.getComputedStyle(canvas).getPropertyValue('--highlight-color'),
                  selectedPoint.value,
                  window.getComputedStyle(canvas).getPropertyValue('--point-text-color'))
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
                        let edge_val = parseInt(prompt('Enter edge value'))
                        let edge = new Edge(selectedPoint, point, edge_val)
                        graph.edges.push(edge)
                        showMessage(`added edge (${edge.start.x}, ${edge.start.y}) - (${edge.end.x}, ${edge.end.y}), value: ${edge.value}`, `log`)
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
                    point = new Point(x, y, graph.points.length + 1)
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

function dijkstra(start_point) {
    let priority_queue = [];
    for (let point of graph.points) {
        point.value = Infinity;
    }
    start_point.value = 0
    priority_queue.push([0, start_point])

    while (priority_queue.length > 0) {
        let u = priority_queue[0][1]
        priority_queue.shift();

        let adj = u.adj()
        let adj_values = u.adj_values()
        for (let i = 0; i < adj.length; i++) {
            let v = adj[i];
            let weight = adj_values[i]

            if (v.value > u.value + weight) {
                v.value = u.value + weight;
                priority_queue.push([v.value, v])
                priority_queue.sort((a, b) => a[0] - b[0]);
            }
        }
    }
}

function proceed() {
    let proceedBtn = document.getElementById('proceed-button')
    if (!proceedBtn.classList.contains('active-button')) {
        return
    }
    // proceedBtn.classList.remove('active-button')
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

            // TODO: fix
            dijkstra(graph.points[0])

            drawGraph(mainCanvas, graph)
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

