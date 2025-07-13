let isOpen = false;
let fontSize = 16;
let highContrast = false;
let darkMode = false;

function toggleMenu() {
  isOpen = !isOpen;
  document.getElementById("acessibilidadeMenu").style.display = isOpen
    ? "block"
    : "none";
}

function increaseFont() {
  fontSize = Math.min(fontSize + 2, 24);
  document.documentElement.style.fontSize = fontSize + "px";
}

function decreaseFont() {
  fontSize = Math.max(fontSize - 2, 12);
  document.documentElement.style.fontSize = fontSize + "px";
}

function toggleContrast() {
  highContrast = !highContrast;
  document.body.classList.toggle("high-contrast", highContrast);
}

function toggleDarkMode() {
  darkMode = !darkMode;
  document.body.classList.toggle("dark-mode", darkMode);
}
