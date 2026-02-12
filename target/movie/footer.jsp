<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    </div>

    <!-- Footer -->
    <footer class="text-center">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <p class="mb-3">
                        <i class="fas fa-film me-2"></i>
                        <strong>CINEMA</strong>
                    </p>
                    <p class="text-secondary small mb-4">
                        发现精彩电影，记录观影足迹
                    </p>
                    <hr class="my-4" style="border-color: var(--color-border);">
                    <p class="text-muted small mb-0">
                        &copy; <%= java.time.Year.now().getValue() %> CINEMA. All Rights Reserved.
                    </p>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JavaScript -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
