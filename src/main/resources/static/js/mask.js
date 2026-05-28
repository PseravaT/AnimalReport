function configurarToggleSenha(buttonId, inputId) {

    const button = document.getElementById(buttonId);
    const input = document.getElementById(inputId);

    if (!button || !input) return;

    button.addEventListener('click', function () {

        const tipoAtual = input.getAttribute('type');

        if (tipoAtual === 'password') {
            input.setAttribute('type', 'text');
            this.innerHTML = '<i class="bi bi-eye-slash"></i>';
        } else {
            input.setAttribute('type', 'password');
            this.innerHTML = '<i class="bi bi-eye"></i>';
        }

    });
}

configurarToggleSenha('togglePassword1', 'senha');
configurarToggleSenha('togglePassword2', 'confirmar_senha');



const telefoneInput = document.getElementById('telefone');

if (telefoneInput) {

    telefoneInput.addEventListener('input', function (e) {

        let valor = e.target.value.replace(/\D/g, '');


        valor = valor.slice(0, 11);

        valor = valor.replace(/^(\d{2})(\d)/g, '($1) $2');
        valor = valor.replace(/(\d{5})(\d)/, '$1-$2');

        e.target.value = valor;

    });

}



const cpfInput = document.getElementById('cpf');

if (cpfInput) {

    cpfInput.addEventListener('input', function (e) {

        let valor = e.target.value.replace(/\D/g, '');

        valor = valor.slice(0, 11);

        valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
        valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
        valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

        e.target.value = valor;

    });

}


const cnpjInput = document.getElementById('cnpj');

if (cnpjInput) {

    cnpjInput.addEventListener('input', function (e) {

        let valor = e.target.value.replace(/\D/g, '');

        valor = valor.slice(0, 14);

        valor = valor.replace(/^(\d{2})(\d)/, '$1.$2');
        valor = valor.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
        valor = valor.replace(/\.(\d{3})(\d)/, '.$1/$2');
        valor = valor.replace(/(\d{4})(\d)/, '$1-$2');

        e.target.value = valor;

    });

}



const emailInput = document.getElementById('email');

if (emailInput) {

    emailInput.addEventListener('blur', function () {

        const email = this.value;

        const valido =
            email.includes('@') &&
            email.includes('.');

        if (!valido) {

            this.classList.add('is-invalid');

        } else {

            this.classList.remove('is-invalid');

        }

    });

}
