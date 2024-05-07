let mapWidth = parseInt(document.querySelector(".map-width").textContent);
let mapHeight = parseInt(document.querySelector(".map-height").textContent);


window.onbeforeunload = function() {
    return "Are you sure you want to navigate away?";
}

function createTable(){
    let  table = document.createElement('table');
    for (let i = 0; i < mapHeight; i++){
        let tr = document.createElement('tr');
        for (let j =0; j<mapWidth; j++){
            let td = document.createElement('td');
            tr.appendChild(td);
        }
        table.appendChild(tr)
    }
    return table;
}
document.querySelector(".map").appendChild(createTable());
let selectedImage = null;

let images = Array.from(document.querySelectorAll(".image"));
let indexImage = "-1";
images.forEach(image => {
    image.addEventListener('click', function() {
        selectedImage = this.src;
        indexImage = images.indexOf(this);
        // alert(indexImage)
    });
});


let cells = Array.from(document.querySelectorAll("td"));
cells.forEach(cell => {
    cell.addEventListener('click', function() {
        if (this.style.backgroundImage) {
            this.style.backgroundImage = '';
            this.style.backgroundColor = 'white';
            this.setAttribute("value", "-1");
        } else if (selectedImage) {
            this.style.backgroundImage = `url(${selectedImage})`;
            this.style.backgroundSize = 'cover';
            this.setAttribute("value", indexImage);
        }
    });
});

let button = document.querySelector("button[type='submit']");

button.addEventListener('click', function() {
    let tableData = [];
    let rowData = [];
    let array = [];
    cells.forEach(cell => {
        let imageIndex = cell.getAttribute("value");
        if (imageIndex === null) rowData.push("-1");
        else rowData.push(imageIndex);
        if ((cells.indexOf(cell) + 1) % mapWidth === 0){
            tableData.push(JSON.stringify(rowData));
            array.push(rowData);
            rowData = [];
        }
    });


    let jsonData = tableData.join('\n')

    let blob = new Blob([jsonData], {type: "application/json"});

    let url = URL.createObjectURL(blob);

    let link = document.createElement('a');
    link.download = 'table_data.json';
    link.href = url;

    document.body.appendChild(link);

    link.click();

    document.body.removeChild(link);

// Create a new canvas with the dimensions of the array
    let canvas = document.createElement('canvas');
    canvas.width = array[0].length;
    canvas.height = array.length;

// Get the 2D rendering context for the canvas
    let ctx = canvas.getContext('2d');

// Iterate over the array
    for (let y = 0; y < array.length; y++) {
        for (let x = 0; x < array[0].length; x++) {
            // Create an RGB string, with red being the value from the array, and green and blue being 0
            let rgb = 'rgb(' + array[y][x] + ', 100, 50)';
            if (array[y][x] === "-1") rgb = 'rgb(0,0,0)';
            // Set the fill style of the context to the RGB string
            ctx.fillStyle = rgb;
            // Draw a pixel at the corresponding position in the canvas
            ctx.fillRect(x, y, 1, 1);
        }
    }

// Get a data URL representing the image
    let dataUrl = canvas.toDataURL();

// Create a new img element and set its src attribute to the data URL
    let img = document.createElement('a');
    img.href = dataUrl;

    img.download = 'tileMap.png'
// Append the img element to the body
    document.body.appendChild(img);


    img.click();

    document.body.removeChild(img);

});


// Assuming 'array' is your 2D array
