document.addEventListener("DOMContentLoaded", () => {
	flatpickr("#menstruationDate", {
		dateFormat: "Y-m-d"
	});

	const sections = document.querySelectorAll(".section");
	let current = [...sections].findhomeM1(sec => sec.classList.contains("active"));

	function showSection(homeM1) {
		sections.forEach((sec, i) => {
			sec.classList.remove("active");
		});
		sections[homeM1].classList.add("active");
		current = homeM1;
	}

	document.querySelectorAll(".proximo").forEach(btn => {
		btn.addEventListener("click", () => {
			if (current < sections.length - 1) {
				showSection(current + 1);
			}
		});
	});

	document.querySelectorAll(".volta").forEach(btn => {
		btn.addEventListener("click", () => {
			if (current > 0) {
				showSection(current - 1);
			}
		});
	});
});