document.addEventListener("DOMContentLoaded", () => {
  // Update current date
  const currentDateElement = document.getElementById("currentDate")
  if (currentDateElement) {
    const now = new Date()
    const options = { year: "numeric", month: "long", day: "numeric" }
    currentDateElement.textContent = now.toLocaleDateString("en-US", options)
  }

  // Smooth scrolling for anchor links
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault()

      const targetId = this.getAttribute("href")
      if (targetId === "#") return

      const targetElement = document.querySelector(targetId)
      if (targetElement) {
        window.scrollTo({
          top: targetElement.offsetTop - 80,
          behavior: "smooth",
        })
      }
    })
  })

  // Form submission handling
  const contactForm = document.getElementById("demoForm")
  const formSuccess = document.getElementById("formSuccess")

  if (contactForm) {
    contactForm.addEventListener("submit", (e) => {
      e.preventDefault()

      // Simulate form submission
      const formData = new FormData(contactForm)
      const formObject = {}

      formData.forEach((value, key) => {
        formObject[key] = value
      })

      console.log("Form submitted:", formObject)

      // Show success message
      contactForm.style.display = "none"
      if (formSuccess) {
        formSuccess.classList.remove("hidden")
      }
    })
  }

  // Add active class to nav items on scroll
  const sections = document.querySelectorAll("section[id]")

  function highlightNavOnScroll() {
    const scrollY = window.pageYOffset

    sections.forEach((section) => {
      const sectionHeight = section.offsetHeight
      const sectionTop = section.offsetTop - 100
      const sectionId = section.getAttribute("id")

      if (scrollY > sectionTop && scrollY <= sectionTop + sectionHeight) {
        document.querySelector('nav a[href="#' + sectionId + '"]')?.classList.add("active")
      } else {
        document.querySelector('nav a[href="#' + sectionId + '"]')?.classList.remove("active")
      }
    })
  }

  window.addEventListener("scroll", highlightNavOnScroll)

  // Initialize the active state
  highlightNavOnScroll()

  // Animation on scroll
  function animateOnScroll() {
    const elements = document.querySelectorAll(".pipeline-step, .feature-card")

    elements.forEach((element) => {
      const position = element.getBoundingClientRect()

      // If element is in viewport
      if (position.top < window.innerHeight && position.bottom >= 0) {
        element.style.opacity = "1"
        element.style.transform = "translateY(0)"
      }
    })
  }

  // Initial setup for animation
  const animatedElements = document.querySelectorAll(".pipeline-step, .feature-card")
  animatedElements.forEach((element) => {
    element.style.opacity = "0"
    element.style.transform = "translateY(20px)"
    element.style.transition = "opacity 0.5s ease, transform 0.5s ease"
  })

  // Run animation on page load and scroll
  window.addEventListener("load", animateOnScroll)
  window.addEventListener("scroll", animateOnScroll)
})
