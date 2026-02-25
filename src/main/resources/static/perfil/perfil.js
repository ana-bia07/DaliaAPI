console.log("JS rodando");

document.addEventListener('DOMContentLoaded', () => {

  const useContraceptive = document.getElementById('useContraceptive');
  const contraceptiveType = document.getElementById('contraceptiveType');
  const labelContraceptiveType = document.getElementById('labelContraceptiveType');

  function toggleContraceptiveType() {
    if (!useContraceptive || !contraceptiveType || !labelContraceptiveType) return;

    if (useContraceptive.value === 'true') {
      contraceptiveType.style.display = 'inline-block';
      labelContraceptiveType.style.display = 'block';
    } else {
      contraceptiveType.style.display = 'none';
      labelContraceptiveType.style.display = 'none';
      contraceptiveType.value = "";
    }
  }

  if (useContraceptive) {
    toggleContraceptiveType();
    useContraceptive.addEventListener('change', toggleContraceptiveType);
  }

  window.mostrarAjuda = function() {
    const caixa = document.getElementById("caixaAjuda");
    if (!caixa) return;
    caixa.style.display = caixa.style.display === "none" ? "block" : "none";
  }

  const openButton = document.querySelector(".setting-item-centered");
  const modal = document.getElementById("pregnancy-modal");
  const closeButton = modal ? modal.querySelector(".close-button") : null;
  const modalContainer = modal ? modal.querySelector(".modal-container") : null;

  if (openButton && modal && closeButton && modalContainer) {

    openButton.addEventListener("click", (event) => {
      event.preventDefault();
      modal.style.display = "flex";
      modalContainer.style.animation = "zoomIn 0.4s ease forwards";
      document.body.style.overflow = "hidden";
    });

    function fecharModal() {
      modalContainer.style.animation = "zoomOut 0.4s ease forwards";
      setTimeout(() => {
        modal.style.display = "none";
        document.body.style.overflow = "auto";
      }, 400);
    }

    closeButton.addEventListener("click", fecharModal);

    modal.addEventListener("click", (e) => {
      if (e.target === modal) fecharModal();
    });
  }

});
